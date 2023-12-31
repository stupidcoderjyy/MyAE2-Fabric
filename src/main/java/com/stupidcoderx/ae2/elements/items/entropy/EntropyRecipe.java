package com.stupidcoderx.ae2.elements.items.entropy;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.recipe.ModRecipe;
import com.stupidcoderx.modding.datagen.recipe.RecipeDef;
import com.stupidcoderx.modding.util.serialize.ArrayVal;
import com.stupidcoderx.modding.util.serialize.ContainerVal;
import com.stupidcoderx.modding.util.serialize.Val;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public class EntropyRecipe extends ModRecipe<EntropyRecipe> {
    private static final int BLOCK = 0, FLUID = 1, ITEM = 2;
    private boolean isCool;
    private Block inputBlock, outputBlock;
    private Fluid inputFluid, outputFluid;
    private List<ItemStack> droppedItems;

    public EntropyRecipe(ResourceLocation loc, RecipeDef<EntropyRecipe> def) {
        super(loc, def);
    }

    public boolean matches(boolean isCool, Block block, Fluid fluid) {
        return isCool == this.isCool && (inputBlock == block || inputFluid == fluid);
    }

    public void apply(Level level, BlockPos pos) {
        if (outputBlock != null) {
            level.setBlockAndUpdate(pos, outputBlock.defaultBlockState());
        }
        if (outputFluid != null) {
            level.setBlockAndUpdate(pos, outputFluid.defaultFluidState().createLegacyBlock());
        }
        if (outputBlock == null && outputFluid == null) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        if (droppedItems != null) {
            Mod.spawnDropItems(level, pos, droppedItems, true);
        }
        SoundEvent sound = isCool ? SoundEvents.BASALT_PLACE : SoundEvents.FIRE_EXTINGUISH;
        Mod.playGlobalSound(level, pos, sound, SoundSource.BLOCKS,
                1.0F, 0.8F, 1.2F);
    }

    @DataGenOnly
    public EntropyRecipe cool(boolean cool) {
        root.newString("mode").set(cool ? "cool" : "heat");
        return this;
    }

    @DataGenOnly
    public EntropyRecipe input(Block block) {
        setType(true, "block");
        root.newString("input").set(BuiltInRegistries.BLOCK.getKey(block));
        return this;
    }

    @DataGenOnly
    public EntropyRecipe input(Fluid fluid) {
        setType(true, "fluid");
        root.newString("input").set(BuiltInRegistries.FLUID.getKey(fluid));
        return this;
    }

    @DataGenOnly
    public EntropyRecipe output(ItemStack ... stacks) {
        setType(false, "item");
        ArrayVal arr = root.newArray("output");
        for (ItemStack stack : stacks) {
            ContainerVal c = arr.addContainer();
            c.newString("id").set(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if (stack.getCount() > 1) {
                c.newInt("count").set(stack.getCount());
            }
        }
        return this;
    }

    @DataGenOnly
    public EntropyRecipe output(Block block) {
        setType(false, "block");
        root.newString("output").set(BuiltInRegistries.BLOCK.getKey(block));
        return this;
    }

    @DataGenOnly
    public EntropyRecipe output(Fluid fluid) {
        root.newString("input").set(BuiltInRegistries.FLUID.getKey(fluid));
        return this;
    }

    @DataGenOnly
    private void setType(boolean input, String type) {
        root.newString(input ? "input_type" : "output_type").set(type);
    }

    @Override
    protected void onDeserialized() {
        isCool = root.get("mode").asString().get().equals("cool");
        int inputType = parseType(root.get("input_type"));
        Val<?,?> inputVal = root.get("input");
        switch (inputType) {
            case BLOCK -> inputBlock = parseBlock(inputVal);
            case FLUID -> inputFluid = parseFluid(inputVal);
        }

        int outputType = parseType(root.get("output_type"));
        Val<?,?> outputVal = root.get("output");
        switch (outputType) {
            case BLOCK -> outputBlock = parseBlock(outputVal);
            case FLUID -> outputFluid = parseFluid(outputVal);
            case ITEM -> {
                droppedItems = new ArrayList<>();
                for (Val<?, ?> valStack : outputVal.asArray().get()) {
                    droppedItems.add(parseStack(valStack.asContainer()));
                }
            }
        }
    }

    private int parseType(Val<?,?> typeVal) {
        String type = typeVal.asString().get();
        if (type.equals("block")) {
            return BLOCK;
        }
        if (type.equals("fluid")) {
            return FLUID;
        }
        return ITEM;
    }

    private Block parseBlock(Val<?,?> val) {
        return BuiltInRegistries.BLOCK.get(Mod.loc(val.asString().get()));
    }

    private Fluid parseFluid(Val<?,?> val) {
        return BuiltInRegistries.FLUID.get(Mod.loc(val.asString().get()));
    }

    private ItemStack parseStack(ContainerVal stackContainer) {
        Item item = BuiltInRegistries.ITEM.get(Mod.loc(stackContainer.get("id").asString().get()));
        Val<?, ?> countVal = stackContainer.get("count");
        return new ItemStack(item, countVal == null ? 1 : countVal.asInt().get());
    }
}