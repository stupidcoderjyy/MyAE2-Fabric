package com.stupidcoderx.modding.datagen;

import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.blockstate.BlockStateProvider;
import com.stupidcoderx.modding.datagen.model.ModelProvider;

public class DataProviders {
    public static final ModelProvider MODEL_ITEM
            = new ModelProvider("item", Mod.ITEM_REGISTRY::buildData);

    public static final ModelProvider MODEL_BLOCK
            = new ModelProvider("block", Mod.BLOCK_REGISTRY::buildData);

    public static final BlockStateProvider BLOCK_STATE = new BlockStateProvider();

    static void init() {
    }
}
