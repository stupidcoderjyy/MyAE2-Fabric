package com.stupidcoderx.ae2.registry.items;

import com.stupidcoderx.ae2.registry.AECreativeTabs;
import com.stupidcoderx.modding.element.item.ItemDef;
import com.stupidcoderx.modding.element.item.ToolItemDef;
import com.stupidcoderx.modding.element.item.ToolType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

public class AETools {
    public static final ToolType TOOL_TYPE_FLUIX = new ToolType(Tiers.IRON).repairIngredient(() -> AEItems.SILICON);

    public static final ItemDef<AxeItem> FLUIX_AXE = tool("fluix_axe",
            new AxeItem(TOOL_TYPE_FLUIX, 6.0F, -3.1F, new Item.Properties()));

    private static <T extends Item> ItemDef<T> tool(String id, T item) {
        ToolItemDef<T> def = new ToolItemDef<>(id, item);
        def.setCreativeTab(AECreativeTabs.MAIN);
        def.setCreativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES);
        return def;
    }

    static void build() {
    }
}
