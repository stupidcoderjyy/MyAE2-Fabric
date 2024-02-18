package com.stupidcoderx.ae2.client.models;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.stupidcoderx.ae2.elements.blocks.glass.QuartzGlassBlock;
import com.stupidcoderx.ae2.elements.blocks.glass.QuartzGlassBlockDef;
import com.stupidcoderx.modding.client.render.IModCustomModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Environment(EnvType.CLIENT)
public class QuartzGlassModel implements IModCustomModel {
    //边框的材质，从0001-1111
    private static final Material[] FRAME_TEXTURES = IntStream.range(1, 16)
            .mapToObj(Integer::toBinaryString)
            .map(s -> Strings.padStart(s, 4, '0'))
            .map(s -> QuartzGlassBlockDef.TEXTURE_BLOCK_LOC.withSuffix("_frame" + s))
            .map(loc -> new Material(TextureAtlas.LOCATION_BLOCKS, loc))
            .toArray(Material[]::new);

    //玻璃花纹的材质
    private static final Material GLASS_TEXTURE =
            new Material(TextureAtlas.LOCATION_BLOCKS, QuartzGlassBlockDef.TEXTURE_BLOCK_LOC);
    //立方体中各个面的四个顶点坐标
    private static final Map<Direction, List<Vector3f>> vertexPoses = genVertexPosMap();
    //玻璃花纹纹理图集，bake方法中产生
    private TextureAtlasSprite glassTexture;
    //边框纹理图集，bake方法中产生
    private TextureAtlasSprite[] frameTextures;
    //渲染配置，bake方法中产生
    private RenderMaterial material;

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> bakedTextureGetter, ModelState modelState, ResourceLocation resourceLocation) {
        this.glassTexture = bakedTextureGetter.apply(GLASS_TEXTURE);
        this.frameTextures = new TextureAtlasSprite[16];
        for (int i = 0; i < FRAME_TEXTURES.length; i++) { //0000默认为null
            this.frameTextures[1 + i] = bakedTextureGetter.apply(FRAME_TEXTURES[i]);
        }
        this.material = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer())
                .materialFinder()
                .disableDiffuse(true)
                .ambientOcclusion(TriState.FALSE)
                .disableColorIndex(true)
                .blendMode(BlendMode.TRANSLUCENT)
                .find();
        return this;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                               Supplier<RandomSource> randomSupplier, RenderContext context) {
        GlassState gs = getGlassState(blockView, pos);
        QuadEmitter emitter = context.getEmitter();
        for (Direction side : Direction.values()) {
            if (gs.adjacentGlassBlocks[side.get3DDataValue()]) {
                continue; //和emitQuad中emitter.cullFace(side)能产生相同的作用
            }
            List<Vector3f> vertexes = vertexPoses.get(side);
            //渲染玻璃花纹
            emitQuad(emitter, side, vertexes, glassTexture);
            //渲染边框
            int mask = gs.masks[side.get3DDataValue()];
            TextureAtlasSprite frameTexture = frameTextures[mask];
            if (frameTexture != null) {
                emitQuad(emitter, side, vertexes, frameTexture);
            }
        }
    }

    private void emitQuad(QuadEmitter emitter, Direction side, List<Vector3f> vertexes,
                          TextureAtlasSprite sprite) {
        float u1 = sprite.getU(0);
        float v1 = sprite.getV(0);
        float u2 = sprite.getU(16);
        float v2 = sprite.getV(16);
        emitter.cullFace(side);
        emitter.material(material);
        emitter.pos(0, vertexes.get(0)).uv(0, u1, v1);
        emitter.pos(1, vertexes.get(1)).uv(1, u1, v2);
        emitter.pos(2, vertexes.get(2)).uv(2, u2, v2);
        emitter.pos(3, vertexes.get(3)).uv(3, u2, v1);
        emitter.color(-1, -1, -1, -1); //设置四个顶点颜色为纯黑
        emitter.emit();
    }

    private GlassState getGlassState(BlockAndTintGetter level, BlockPos pos) {
        GlassState res = new GlassState(pos.getX(), pos.getY(), pos.getZ());
        for (Direction f : Direction.values()) {
            res.masks[f.get3DDataValue()] = makeBitmask(level, pos, f);
            res.adjacentGlassBlocks[f.get3DDataValue()] = hasAdjacentGlass(level, pos, f);
        }
        return res;
    }

    private int makeBitmask(BlockAndTintGetter level, BlockPos pos, Direction side) {
        return switch (side) {
            case DOWN -> makeBitmask(level, pos, Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST);
            case UP -> makeBitmask(level, pos, Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST);
            case NORTH -> makeBitmask(level, pos, Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST);
            case SOUTH -> makeBitmask(level, pos, Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST);
            case WEST -> makeBitmask(level, pos, Direction.UP, Direction.SOUTH, Direction.DOWN, Direction.NORTH);
            case EAST -> makeBitmask(level, pos, Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH);
        };
    }

    private int makeBitmask(BlockAndTintGetter level, BlockPos pos,
                            Direction up, Direction right, Direction down, Direction left) {
        int bitmask = 0;
        if (!hasAdjacentGlass(level, pos, up)) {
            bitmask |= 1; // up ---X
        }
        if (!hasAdjacentGlass(level, pos, right)) {
            bitmask |= 2; // right --X-
        }
        if (!hasAdjacentGlass(level, pos, down)) {
            bitmask |= 4; // down -X--
        }
        if (!hasAdjacentGlass(level, pos, left)) {
            bitmask |= 8; // left X---
        }
        return bitmask;
    }

    private boolean hasAdjacentGlass(BlockAndTintGetter level, BlockPos pos, Direction side) {
        return level.getBlockState(pos.relative(side)).getBlock() instanceof QuartzGlassBlock;
    }

    private static class GlassState {
        final int x;
        final int y;
        final int z;

        final int[] masks = new int[6];
        final boolean[] adjacentGlassBlocks = new boolean[6];

        GlassState(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.frameTextures[this.frameTextures.length - 1]; //四面都有边框
    }

    private static Map<Direction, List<Vector3f>> genVertexPosMap() {
        Map<Direction, List<Vector3f>> res = new EnumMap<>(Direction.class);
        for (Direction facing : Direction.values()) {
            List<Vector3f> corners;
            float offset = facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 0 : 1;
            //下面的顺序可以看net.minecraft.client.renderer.FaceInfo
            corners = switch (facing.getAxis()) {
                case X -> Lists.newArrayList(
                        new Vector3f(offset, 1, 1),
                        new Vector3f(offset, 0, 1),
                        new Vector3f(offset, 0, 0),
                        new Vector3f(offset, 1, 0));
                case Y -> Lists.newArrayList(
                        new Vector3f(1, offset, 1),
                        new Vector3f(1, offset, 0),
                        new Vector3f(0, offset, 0),
                        new Vector3f(0, offset, 1));
                case Z -> Lists.newArrayList(
                        new Vector3f(0, 1, offset),
                        new Vector3f(0, 0, offset),
                        new Vector3f(1, 0, offset),
                        new Vector3f(1, 1, offset));
            };
            if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                corners = Lists.reverse(corners);
            }
            res.put(facing, corners);
        }
        return res;
    }
}
