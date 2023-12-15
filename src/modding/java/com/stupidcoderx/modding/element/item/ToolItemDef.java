package com.stupidcoderx.modding.element.item;

import com.stupidcoderx.modding.datagen.DataProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ToolItemDef<T extends Item> extends ItemDef<ToolItemDef<T>, T> {
    public ToolItemDef(ResourceLocation loc, T item) {
        super(loc, item);
    }

    public ToolItemDef(String id, T item) {
        super(id, item);
    }

    @Override
    protected void provideModel() {
        DataProviders.MODEL_ITEM.getOrCreateModel(loc)
                .parent("minecraft:item/handheld")
                .texture("layer0", loc);
    }
}
