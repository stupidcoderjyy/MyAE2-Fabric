package com.stupidcoderx.ae2.registry.items;

import com.stupidcoderx.ae2.elements.items.PaintBallItemDef;
import com.stupidcoderx.ae2.registry.AECreativeTabs;
import com.stupidcoderx.modding.element.item.ItemDef;
import net.minecraft.world.item.Item;

public class AEItems {
    public static final ItemDef<Item> SILICON = simple("silicon");

    static {
        PaintBallItemDef.create();
        AETools.build();
    }

    private static ItemDef<Item> simple(String id) {
        ItemDef<Item> def = new ItemDef<>(id, new Item(new Item.Properties()));
        def.setCreativeTab(AECreativeTabs.MAIN);
        return def;
    }

    public static void build() {
    }
}
