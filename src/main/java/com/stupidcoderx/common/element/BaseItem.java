package com.stupidcoderx.common.element;

import com.stupidcoderx.common.core.IRegistry;
import com.stupidcoderx.common.core.Mod;
import com.stupidcoderx.common.datagen.generators.ItemModelGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BaseItem<I extends BaseItem<I>> extends Item implements IRegistry {
    public final ResourceLocation location;

    public BaseItem(Properties properties, ResourceLocation location) {
        super(properties);
        this.location = location;
        Mod.ITEM_REGISTRY.add(this);
    }

    public BaseItem(ResourceLocation location) {
        this(new Properties(), location);
    }

    public BaseItem(String id) {
        this(new ResourceLocation(Mod.getModId(), id));
    }

    public final I setCreativeTab(ModCreativeTab tab) {
        tab.addItem(this);
        return self();
    }

    public final I setCreativeTab(ResourceKey<CreativeModeTab> tabKey) {
        ItemGroupEvents.modifyEntriesEvent(tabKey).register(entries -> entries.accept(this));
        return self();
    }

    public ItemStack stack(int size) {
        return new ItemStack(this, size);
    }

    @Override
    public void commonRegister() {
        Registry.register(BuiltInRegistries.ITEM, location, this);
    }

    @Override
    public void buildData() {
        ItemModelGenerator.getInstance()
                .model(location)
                .parent("minecraft:item/generated")
                .texture("layer0", location);
    }

    private I self() {
        return (I) this;
    }
}
