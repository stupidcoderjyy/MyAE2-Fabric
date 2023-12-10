package com.stupidcoderx.modding.element.item;

import com.stupidcoderx.modding.element.ModCreativeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class SimpleItemDef<I extends Item> extends ItemDef<SimpleItemDef<I>, I>{
    private SimpleItemDef(ResourceLocation loc, I item) {
        super(loc, item);
    }

    private SimpleItemDef(String id, I item) {
        super(id, item);
    }

    public static SimpleItemDef<Item> create(String id) {
        return new SimpleItemDef<>(id, new Item(new Item.Properties()));
    }

    public static SimpleItemDef<Item> create(String id, ModCreativeTab tab) {
        return new SimpleItemDef<>(id, new Item(new Item.Properties())).creativeTab(tab);
    }

    public static <T extends Item> SimpleItemDef<T> create(String id, T item) {
        return new SimpleItemDef<>(id, item);
    }
}
