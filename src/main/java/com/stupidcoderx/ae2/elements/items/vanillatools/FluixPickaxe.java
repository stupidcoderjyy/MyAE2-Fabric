package com.stupidcoderx.ae2.elements.items.vanillatools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;

public class FluixPickaxe extends PickaxeItem implements IFluixTool {
    public FluixPickaxe(int damage, float speed, Properties p) {
        super(ToolTypes.FLUIX, damage, speed, p);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
