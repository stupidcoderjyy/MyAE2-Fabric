package com.stupidcoderx.modding.datagen.model;

import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.ModDataProvider;
import com.stupidcoderx.modding.datagen.ResourceType;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ModelProvider extends ModDataProvider<ModelProvider> {
    private final Map<ResourceLocation, ModelBuilder> models = new HashMap<>();
    private final Function<ResourceLocation, ModelBuilder> defaultModelBuilderSupplier;
    private final String prefix;

    public ModelProvider(String pathPrefix) {
        super(ResourceType.MODEL);
        this.prefix = pathPrefix;
        this.defaultModelBuilderSupplier = loc -> new ModelBuilder(pathPrefix, loc);
    }

    public ModelBuilder model(ResourceLocation loc) {
        loc = Mod.expandLoc(prefix, loc);
        return models.computeIfAbsent(loc, defaultModelBuilderSupplier);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return getCollectedTask(models.entrySet(),
                e -> getJsonWritingTask(e.getKey(), e.getValue().toJson(), cache));
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "(" + prefix + ")";
    }
}
