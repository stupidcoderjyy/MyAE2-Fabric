package com.stupidcoderx.modding.datagen;

import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.blockstate.BlockStateProvider;
import com.stupidcoderx.modding.datagen.model.ModelProvider;
import com.stupidcoderx.modding.datagen.recipe.RecipeProvider;

public class DataProviders {
    public static final ModelProvider MODEL_ITEM
            = new ModelProvider("item", Mod.ITEM_REGISTRY::provideData);

    public static final ModelProvider MODEL_BLOCK
            = new ModelProvider("block", Mod.BLOCK_REGISTRY::provideData);

    public static final BlockStateProvider BLOCK_STATE = new BlockStateProvider();

    public static final RecipeProvider RECIPE = new RecipeProvider(Mod.RECIPE_REGISTRY::provideData);

    static void init() {
    }
}
