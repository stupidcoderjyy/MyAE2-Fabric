package com.stupidcoderx.modding.datagen;

import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.blockstate.BlockStateProvider;
import com.stupidcoderx.modding.datagen.model.ModelProvider;
import net.minecraft.data.DataGenerator;

import java.util.ArrayList;
import java.util.List;

public class DataProviders {
    static List<ModDataProvider<?>> providers = new ArrayList<>();

    public static final ModelProvider ITEM
            = new ModelProvider("item", Mod.ITEM_REGISTRY::buildData);

    public static final ModelProvider BLOCK
            = new ModelProvider("block", Mod.BLOCK_REGISTRY::buildData);

    public static final BlockStateProvider BLOCK_STATE = new BlockStateProvider();

    static void init(DataGenerator.PackGenerator pack) {
        providers.forEach(p -> pack.addProvider(p::init));
        providers = null;
    }
}
