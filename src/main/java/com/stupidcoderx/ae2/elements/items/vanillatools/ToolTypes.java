package com.stupidcoderx.ae2.elements.items.vanillatools;

import com.stupidcoderx.ae2.registry.AEItems;
import com.stupidcoderx.modding.element.item.ToolType;
import net.minecraft.world.item.Tiers;

class ToolTypes {
    static final ToolType FLUIX = new ToolType(Tiers.IRON).repairIngredient(() -> AEItems.FLUIX_CRYSTAL);
}
