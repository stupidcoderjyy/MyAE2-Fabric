package com.stupidcoderx.modding.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ModDataProvider<T extends ModDataProvider<T>> implements DataProvider {
    protected PackOutput output;

    public ModDataProvider() {
        DataProviders.providers.add(this);
    }

    public T init(PackOutput output) {
        this.output = output;
        return (T)this;
    }

    protected CompletableFuture<?> getJsonWritingTask(
            ResourceLocation loc,
            JsonObject obj,
            CachedOutput cache,
            ResourceType type) {
        Path target = output.getOutputFolder(type.type())
                .resolve(loc.getNamespace())
                .resolve(type.pathPrefix())
                .resolve(loc.getPath() + type.pathSuffix());
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
}
