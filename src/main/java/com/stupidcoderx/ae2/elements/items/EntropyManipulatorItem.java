package com.stupidcoderx.ae2.elements.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class EntropyManipulatorItem extends Item {
    private static final String CUR_POWER_NBT = "curPower";
    private static final int MAX_POWER = 10;

    public EntropyManipulatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return Mth.hsvToRgb(1.0f / 3.0f, 1.0f, 1.0f);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        double filled = getPower(itemStack) / (double)MAX_POWER;
        return Mth.clamp((int)(filled * 13), 0, 13);
    }

    private void setPower(ItemStack stack, int p) {
        stack.getOrCreateTag().putInt(CUR_POWER_NBT, p);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        ItemStack stack = useOnContext.getItemInHand();
        Level world = useOnContext.getLevel();
        int power = getPower(stack);
        if (power == 0) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            setPower(stack, power - 1);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    private int getPower(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            return tag.getInt(CUR_POWER_NBT);
        }
        return MAX_POWER;
    }
}
