package com.stupidcoderx.ae2.elements.items.compass;

import com.stupidcoderx.modding.client.render.IModCustomModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("removal")
@Environment(EnvType.CLIENT)
public class CompassModel implements IModCustomModel {
    private BakedModel base, pointer;

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Set.of(CompassItemDef.BASE_LOC, CompassItemDef.POINTER_LOC);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        base = modelBaker.bake(CompassItemDef.BASE_LOC, modelState);
        pointer = modelBaker.bake(CompassItemDef.POINTER_LOC, modelState);
        return this;
    }

    private final Quaternionf rotator = new Quaternionf();
    private final Vector3f pos = new Vector3f();
    private float pointerRotation;

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.bakedModelConsumer().accept(base);
        context.pushTransform(quad -> {
            //对指南针每个面上的四个坐标进行旋转，由于指南针有4个面，故这个方法会被调用四次
            rotator.rotationY(pointerRotation);
            for (int i = 0; i < 4; i++) {
                quad.copyPos(i, pos);
                //在模型坐标系中，坐标原点在左下角，需要把原点移到NY面的中央再执行旋转
                pos.add(-0.5f, 0, -0.5f);
                pos.rotate(rotator);
                pos.add(0.5f, 0, 0.5f);
                quad.pos(i, pos);
            }
            return true;
        });
        context.bakedModelConsumer().accept(pointer);
        context.popTransform();
    }

    @Override
    public ItemOverrides getOverrides() {
        return new ItemOverrides() {
            @Nullable
            @Override
            public BakedModel resolve(BakedModel bakedModel, ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                pointerRotation = getCompassRotation(clientLevel, livingEntity);
                return bakedModel;
            }
        };
    }

    private static float getCompassRotation(ClientLevel level, LivingEntity entity) {
        if (level != null && entity != null && level.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD) {
            GlobalPos gPos = CompassItem.getSpawnPosition(level);
            if (gPos != null) {
                BlockPos pos = gPos.pos();
                double ax = entity.getX(), az = entity.getZ();
                int bx = pos.getX(), bz = pos.getZ();
                float r1 = (float) Math.atan2(az - bz, bx - ax);
                float r2 = entity.getYRot() / 180f * Mth.PI;
                return r1 + r2 - Mth.PI / 2;
            }
        }
        return (System.currentTimeMillis() % 3000) / 3000.0f * Mth.PI * 2;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.base.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.base.getTransforms();
    }
}
