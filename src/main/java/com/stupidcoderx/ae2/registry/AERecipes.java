package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.recipes.EntropyRecipe;
import com.stupidcoderx.modding.datagen.recipe.RecipeDef;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class AERecipes {
    public static final RecipeDef<EntropyRecipe> ENTROPY = new RecipeDef<>("entropy", EntropyRecipe::new)
            .register("heat/grass_block_dirt", r -> r.cool(false).input(Blocks.GRASS_BLOCK).output(Blocks.DIRT))
            .register("heat/water_air", r -> r.cool(false).input(Fluids.WATER).output(Blocks.AIR))
            .register("cool/water_ice", r -> r.cool(true).input(Fluids.WATER).output(Blocks.ICE));

    public static void build() {
    }
}
