package com.stupidcoderx.modding.element;

import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BaseItem extends Item implements IRegistry {
    public final ResourceLocation registryLoc;
    
    public BaseItem(Properties properties, ResourceLocation location) {
        super(properties);
        this.registryLoc = location;
        Mod.ITEM_REGISTRY.add(this);
    }

    public BaseItem(Properties properties, String id) {
        this(properties, Mod.modLoc(id));
    }

    public BaseItem(ResourceLocation loc) {
        this(new Properties(), loc);
    }

    public BaseItem(String id) {
        this(new Properties(), Mod.modLoc(id));
    }

    public BaseItem creativeTab(ModCreativeTab tab) {
        tab.add(this);
        return this;
    }

    public BaseItem creativeTab(ResourceKey<CreativeModeTab> tabKey) {
        ItemGroupEvents.modifyEntriesEvent(tabKey)
                .register(entries -> entries.accept(this));
        return this;
    }

    public ItemStack stack(int size) {
        return new ItemStack(this, size);
    }

    @Override
    public void commonRegister() {
        Registry.register(BuiltInRegistries.ITEM, registryLoc, this);
    }

    @Override
    public void buildData() {
        generateModel();
    }

    protected void generateModel() {
        DataProviders.ITEM.getOrCreateModel(registryLoc)
                .parent("minecraft:item/generated")
                .texture("layer0", registryLoc);
    }
}
