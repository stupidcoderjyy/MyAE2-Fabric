package com.stupidcoderx.ae2.elements.items.compass;

import com.stupidcoderx.modding.client.render.BasicUnbakedModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class CompassUnbakedModel implements BasicUnbakedModel {
    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        BakedModel base = modelBaker.bake(CompassItemDef.BASE, modelState);
        BakedModel pointer = modelBaker.bake(CompassItemDef.POINTER, modelState);
        return new CompassBakedModel(base, pointer);
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Set.of(CompassItemDef.BASE, CompassItemDef.POINTER);
    }
}
