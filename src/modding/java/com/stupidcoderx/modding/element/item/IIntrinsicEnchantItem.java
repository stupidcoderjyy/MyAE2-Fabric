package com.stupidcoderx.modding.element.item;

import com.stupidcoderx.modding.mixin.EnchantmentHelperMixin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 用于实现物品的初始附魔，继承此接口即可生效。虽然看起来是“初始”的附魔，
 * 但实际上物品并没有真的拥有附魔，而是在MC尝试获取附魔的时候手动返回<p>
 *
 * 如果通过其他方式获得了与初始附魔相同类型的附魔，则初始附魔会失效。如：
 * 初始时运1 + 铁砧附魔的时运1 = 时运1
 * @see EnchantmentHelperMixin
 * @see net.minecraft.world.item.enchantment.EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)
 */
public interface IIntrinsicEnchantItem {
    int getIntrinsicEnchantLevel(ItemStack stack, Enchantment enchantment);
}
