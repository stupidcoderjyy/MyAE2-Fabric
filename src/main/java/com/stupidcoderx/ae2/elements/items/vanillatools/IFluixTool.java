package com.stupidcoderx.ae2.elements.items.vanillatools;

import com.stupidcoderx.modding.element.item.IIntrinsicEnchantItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

interface IFluixTool extends IIntrinsicEnchantItem {
    @Override
    default int getIntrinsicEnchantLevel(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BLOCK_FORTUNE ? 10 : 0;
    }
}
