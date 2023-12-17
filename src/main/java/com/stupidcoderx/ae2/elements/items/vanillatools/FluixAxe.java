package com.stupidcoderx.ae2.elements.items.vanillatools;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class FluixAxe extends AxeItem implements IFluixTool {
    public FluixAxe(float damage, float speed, Properties p) {
        super(ToolTypes.FLUIX, damage, speed, p);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
