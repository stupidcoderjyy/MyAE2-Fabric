package com.stupidcoderx.modding.datagen.tag;

import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.ModDataProvider;
import com.stupidcoderx.modding.datagen.ResourceType;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TagsProvider<T extends Tag<T,?>> extends ModDataProvider<TagsProvider<T>> {
    final Map<ResourceLocation, T> tags = new HashMap<>();
    final String type;
    private final Supplier<T> builder;

    public TagsProvider(String type, Supplier<T> builder) {
        super(ResourceType.TAG);
        this.type = type;
        this.builder = builder;
    }

    public T of(ResourceLocation loc) {
        return tags.computeIfAbsent(loc, k -> builder.get());
    }

    public T of(TagKey<?> key) {
        return of(key.location());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return getCollectedTask(tags.entrySet(), entry -> {
            ResourceLocation loc = Mod.expandLoc(type, entry.getKey());
            return getJsonWritingTask(loc, entry.getValue().toJson(), cachedOutput);
        });
    }

    @Override
    public String getName() {
        return "TagsProvider[" + type + "]";
    }
}
