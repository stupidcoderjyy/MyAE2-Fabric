package com.stupidcoderx.modding.element.item;

import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.element.ModCreativeTab;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public abstract class ItemDef<D extends ItemDef<D, I>, I extends Item> implements IRegistry, ItemLike {
    public final I item;
    public final ResourceLocation loc;

    public ItemDef(ResourceLocation loc, I item) {
        this.item = item;
        this.loc = loc;
        Mod.ITEM_REGISTRY.add(this);
    }

    public ItemDef(String id, I item) {
        this(Mod.modLoc(id), item);
    }

    public D creativeTab(ModCreativeTab tab) {
        tab.add(this);
        return self();
    }

    public D creativeTab(ResourceKey<CreativeModeTab> tabKey) {
        ItemGroupEvents.modifyEntriesEvent(tabKey)
                .register(entries -> entries.accept(this));
        return self();
    }

    @Override
    public I asItem() {
        return item;
    }

    public ItemStack stack(int size) {
        return new ItemStack(item, size);
    }

    @Override
    public void commonRegister() {
        Registry.register(BuiltInRegistries.ITEM, loc, item);
    }

    @Override
    public void buildData() {
        generateModel();
    }

    protected void generateModel() {
        DataProviders.MODEL_ITEM.getOrCreateModel(loc)
                .parent("minecraft:item/generated")
                .texture("layer0", loc);
    }

    protected final D self() {
        return (D)this;
    }
}
