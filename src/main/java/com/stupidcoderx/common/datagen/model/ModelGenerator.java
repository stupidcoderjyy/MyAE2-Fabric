package com.stupidcoderx.common.datagen.model;

import com.google.common.base.Preconditions;
import com.stupidcoderx.common.core.Mod;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ModelGenerator<G extends ModelGenerator<G>> implements DataProvider {
    protected final Map<ResourceLocation, ModelBuilder> models = new HashMap<>();
    protected final Function<ResourceLocation, ModelBuilder> defaultModelBuilderSupplier;
    protected final PackOutput output;
    protected final String folder;
    protected final ResourceFileHelper helper;
    private final IGeneratorDataRegistry registry;
    private ModelBuilder curBuilder = null;

    protected ModelGenerator(
            PackOutput output,
            String folder,
            Function<ResourceLocation, ModelBuilder> defaultModelBuilderSupplier,
            ResourceFileHelper helper,
            IGeneratorDataRegistry registry) {
        this.defaultModelBuilderSupplier = defaultModelBuilderSupplier;
        this.output = output;
        this.folder = folder;
        this.helper = helper;
        this.registry = registry;
    }

    /**
     * 获取或创建对象的模型文件构造器
     * @param target 对象的资源路径，如："minecraft:item/generated", "silicon"。如果传入的名称不包含'/'，则自动添加父目录；
     *               如果传入名称不包含命名空间，则默认为模组命名空间
     * @return  自己
     */
    public G model(String target) {
        ResourceLocation loc = expandLoc(target);
        helper.trackGenerated(loc, ResourceType.MODEL);
        curBuilder = models.computeIfAbsent(loc, defaultModelBuilderSupplier);
        return self();
    }
    
    /**
     * 获取或创建对象的模型文件构造器
     * @param target 对象的资源路径，如果{@link ResourceLocation#getPath()}不包含'/'，则自动添加父目录；
     * @return 自己
     */
    public G model(ResourceLocation target) {
        ResourceLocation loc = expandLoc(target);
        helper.trackGenerated(loc, ResourceType.MODEL);
        curBuilder = models.computeIfAbsent(loc, defaultModelBuilderSupplier);
        return self();
    }

    /**
     * 为当前的模型文件构造器设置父模型
     * @param parent 父模型的资源路径，如果{@link ResourceLocation#getPath()}不包含'/'，则自动添加父目录；
     * @return 自己
     */
    public G parent(ResourceLocation parent) {
        assertModel();
        curBuilder.setParent(getExistingFile(parent.getPath(), parent.getNamespace()));
        return self();
    }

    /**
     * 为当前的模型文件构造器设置父模型
     * @param parent 父模型的资源路径，解析规则见{@link #model(String)}
     * @return 自己
     */
    public G parent(String parent) {
        assertModel();
        curBuilder.setParent(getExistingFile(parent, null));
        return self();
    }


    /**
     * 向当前的模型文件构造器添加材质
     * @param key 材质的键，如"layer0"
     * @param texture 材质路径，解析规则见{@link #model(String)}
     * @return 自己
     */
    public G texture(String key, String texture) {
        assertModel();
        curBuilder.texture(key, expandLoc(texture));
        return self();
    }

    /**
     * 向当前的模型文件构造器添加材质
     * @param key 材质的键，如"layer0"
     * @param texture 材质路径
     * @return 自己
     */
    public G texture(String key, ResourceLocation texture) {
        assertModel();
        curBuilder.texture(key, expandLoc(texture));
        return self();
    }
    
    private G self() {
        return (G) this;
    }

    private ExistingModelFile getExistingFile(String target, @Nullable String namespace) {
        ResourceLocation loc = expandLoc(target, namespace);
        var file = new ExistingModelFile(loc, helper);
        file.ensureExistence();
        return file;
    }

    private ResourceLocation expandLoc(String target, @Nullable String namespace) {
        if (namespace == null) {
            namespace = Mod.getModId();
        }
        int i = target.indexOf(":");
        if (i > 0) {
            namespace = target.substring(0, i);
            target = target.substring(i + 1);
        }
        return new ResourceLocation(namespace, target.contains("/") ? target : folder + "/" + target);
    }

    private ResourceLocation expandLoc(String target) {
        return expandLoc(target, null);
    }

    private ResourceLocation expandLoc(ResourceLocation loc) {
        return expandLoc(loc.getPath(), loc.getNamespace());
    }

    private void assertModel() {
        Preconditions.checkState(curBuilder != null, "model builder not initialized");
    }

    /**
     * 获得任务对象，任务为将所有的模型{@link ModelBuilder}输出为json文件
     * @param cache mc自带的输出器
     * @return 任务对象
     */
    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        registry.register();
        CompletableFuture<?>[] futures = new CompletableFuture<?>[models.size()];
        int i = 0;
        for (var e : models.entrySet()) {
            ModelBuilder model = e.getValue();
            ResourceLocation loc = model.location;
            Path target = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(loc.getNamespace())
                    .resolve("models")
                    .resolve(loc.getPath() + ".json");
            futures[i++] = DataProvider.saveStable(cache, model.toJson(), target);
        }
        return CompletableFuture.allOf(futures);
    }
}
