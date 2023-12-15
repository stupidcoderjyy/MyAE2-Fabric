package com.stupidcoderx.modding.datagen.model;

import com.google.common.base.Preconditions;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.IGeneratorDataRegistry;
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

    public ModelProvider(String pathPrefix, IGeneratorDataRegistry registry) {
        super(ResourceType.MODEL, registry);
        this.prefix = pathPrefix;
        this.defaultModelBuilderSupplier = loc -> new ModelBuilder(pathPrefix, loc);
    }

    /**
     * 获取或创建对象的模型文件构造器
     * @param loc 对象的资源路径
     * @return 自己
     */
    public ModelBuilder getOrCreateModel(ResourceLocation loc) {
        Preconditions.checkState(output != null, "generator not initialized");
        loc = ModelBuilder.expandLoc(prefix, loc);
        return models.computeIfAbsent(loc, defaultModelBuilderSupplier);
    }

    /**
     *
     * @see #getOrCreateModel(ResourceLocation)
     */
    public ModelBuilder getOrCreateModel(String loc) {
        return getOrCreateModel(Mod.modLoc(loc));
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
