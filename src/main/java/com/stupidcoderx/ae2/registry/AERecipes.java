package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.recipes.EntropyRecipe;
import com.stupidcoderx.modding.datagen.recipe.RecipeDef;
import net.minecraft.world.level.block.Blocks;

public class AERecipes {
    public static final RecipeDef<EntropyRecipe> ENTROPY = new RecipeDef<>("entropy", EntropyRecipe::new)
            .register("grass_block_dirt", r -> r.coolMode(true).input(Blocks.GRASS_BLOCK).output(Blocks.DIRT));

    public static void build() {
    }
}
