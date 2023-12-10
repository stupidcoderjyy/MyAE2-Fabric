package com.stupidcoderx.modding.datagen;

import com.google.gson.JsonElement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ModDataProvider<T extends ModDataProvider<T>> implements DataProvider {
    static List<ModDataProvider<?>> providers = new ArrayList<>();
    protected PackOutput output;
    private final ResourceType resourceType;
    private final IGeneratorDataRegistry registry;

    public ModDataProvider(ResourceType resourceType, IGeneratorDataRegistry registry) {
        this.registry = registry;
        providers.add(this);
        this.resourceType = resourceType;
    }

    public T init(PackOutput output) {
        this.output = output;
        registry.register();
        return (T)this;
    }

    protected CompletableFuture<?> getJsonWritingTask(
            ResourceLocation loc,
            JsonElement obj,
            CachedOutput cache) {
        Path target = output.getOutputFolder(resourceType.type)
                .resolve(loc.getNamespace())
                .resolve(resourceType.pathPrefix)
                .resolve(loc.getPath() + resourceType.pathSuffix);
        return DataProvider.saveStable(cache, obj, target);
    }

    protected <U> CompletableFuture<?> getCollectedTask(
            Collection<U> c,
            Function<U, CompletableFuture<?>> supplier) {
        CompletableFuture<?>[] futures = new CompletableFuture<?>[c.size()];
        int i = 0;
        for (var e : c) {
            futures[i++] = supplier.apply(e);
        }
        return CompletableFuture.allOf(futures);
    }

    static void register(DataGenerator.PackGenerator pack) {
        providers.forEach(p -> pack.addProvider(p::init));
        providers = null;
    }
}
