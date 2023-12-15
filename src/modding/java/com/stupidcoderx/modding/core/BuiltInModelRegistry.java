package com.stupidcoderx.modding.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BuiltInModelRegistry implements IRegistry, ModelResourceProvider {
    private final Map<ResourceLocation, UnbakedModel> models = new HashMap<>();
    public static final BuiltInModelRegistry INSTANCE = new BuiltInModelRegistry();

    private BuiltInModelRegistry() {
    }

    public void register(ResourceLocation loc, UnbakedModel model) {
        models.put(loc, model);
    }

    @Override
    public void clientRegister() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(m -> this);
    }

    @Override
    public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) throws ModelProviderException {
        return models.get(resourceId);
    }

    @Override
    public String toString() {
        return "builtInModel";
    }
}
