package com.stupidcoderx.modding.datagen.blockstate;

import com.google.common.base.Preconditions;
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
        super(ResourceType.BLOCK_STATE, () -> {});
    }

    public VariantsBlockStateBuilder variants(ResourceLocation loc) {
        Preconditions.checkArgument(!stateBuilders.containsKey(loc),
                "block state builder for " + loc + " already exists");
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
