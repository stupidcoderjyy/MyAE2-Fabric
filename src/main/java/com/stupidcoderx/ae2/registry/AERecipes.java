package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.items.entropy.EntropyRecipe;
import com.stupidcoderx.modding.datagen.recipe.RecipeDef;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class AERecipes {
    public static final RecipeDef<EntropyRecipe> ENTROPY = new RecipeDef<>("entropy", EntropyRecipe::new)
            .register("heat/water_air", r -> r.cool(false).input(Fluids.WATER).output(Blocks.AIR))
            .register("heat/ice_water", r -> r.cool(false).input(Blocks.ICE).output(Blocks.WATER))
            .register("cool/water_ice", r -> r.cool(true).input(Fluids.WATER).output(Blocks.ICE))
            .register("cool/flowing_water_snowball",
                    r -> r.cool(true).input(Fluids.FLOWING_WATER).output(new ItemStack(Items.SNOWBALL)));

    public static void build() {
    }
}
