package com.stupidcoderx.ae2.elements.items.compass;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Supplier;

public class CompassBakedModel implements BakedModel, FabricBakedModel {
    private final BakedModel base, pointer;
    private float pointerRot;

    public CompassBakedModel(BakedModel base, BakedModel pointer) {
        this.base = base;
        this.pointer = pointer;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                               Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.bakedModelConsumer().accept(base);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.bakedModelConsumer().accept(base);
        context.pushTransform(quad -> {
            Quaternionf quaternion = new Quaternionf().rotationY(pointerRot);
            Vector3f pos = new Vector3f();
            for (int i = 0; i < 4; i++) {
                quad.copyPos(i, pos);
                //在模型坐标系中，坐标原点在左下角，需要把原点移到NY面的中央再执行旋转
                pos.add(-0.5f, 0, -0.5f);
                pos.rotate(quaternion);
                pos.add(0.5f, 0, 0.5f);
                quad.pos(i, pos);
            }
            return true;
        });
        context.bakedModelConsumer().accept(pointer);
        context.popTransform();
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.base.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.base.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return new ItemOverrides() {
            @Nullable
            @Override
            public BakedModel resolve(BakedModel bakedModel, ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                pointerRot = getCompassRotation(clientLevel, livingEntity);
                return bakedModel;
            }
        };
    }

    private static float getCompassRotation(@Nullable ClientLevel level, @Nullable LivingEntity entity) {
        if (level != null && entity != null) {
            if (level.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD) {
                GlobalPos gPos = CompassItem.getSpawnPosition(level);
                if (gPos != null) {
                    BlockPos pos = gPos.pos();
                    double ax = entity.getX(), az = entity.getZ();
                    int bx = pos.getX(), bz = pos.getZ();
                    float r1 = (float) Math.atan2(az - bz, bx - ax);
                    float r2 = entity.getYRot() / 180f * Mth.PI + Mth.PI / 2;
                    return r1 + r2;
                }
            }
        }

        long timeMillis = System.currentTimeMillis();
        // 3 seconds per full rotation
        timeMillis %= 3000;
        return timeMillis / 3000.f * Mth.PI * 2;
    }
}
