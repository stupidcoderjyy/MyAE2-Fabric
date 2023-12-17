package com.stupidcoderx.modding.datagen.blockstate;

import com.stupidcoderx.modding.datagen.ModDataProvider;
import com.stupidcoderx.modding.datagen.ResourceType;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BlockStateProvider extends ModDataProvider<BlockStateProvider> {
    private final Map<ResourceLocation, IBlockStateBuilder> stateBuilders = new HashMap<>();

    public BlockStateProvider() {
        super(ResourceType.BLOCK_STATE);
    }

    /**
     * 创建一个枚举方块状态文件。枚举方块状态文件定义了方块所有属性的所有可能情况
     * @param loc 方块资源路径
     * @return 构造器
     */
    public VariantsBlockStateBuilder variants(ResourceLocation loc) {
        ensureUnlocked();
        var builder = new VariantsBlockStateBuilder();
        stateBuilders.put(loc, builder);
        return builder;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return getCollectedTask(stateBuilders.entrySet(),
                e -> getJsonWritingTask(e.getKey(), e.getValue().toJson(), cache));
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
