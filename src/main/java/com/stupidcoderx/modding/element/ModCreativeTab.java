package com.stupidcoderx.modding.element;

import com.stupidcoderx.ae2.core.AE;
import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTab implements IRegistry {
    private List<ItemLike> displayItems = new ArrayList<>();
    private final Supplier<ItemStack> iconSupplier;
    private final Component title;
    private final String id;

    public ModCreativeTab(Supplier<ItemStack> icon, String id, Component title) {
        this.iconSupplier = icon;
        this.title = title;
        this.id = id;
        Mod.CREATIVE_TAB_REGISTRY.add(this);
    }

    public ModCreativeTab(Supplier<ItemStack> icon, String id) {
        this(icon, id, Component.literal(id));
    }

    public void add(ItemLike item) {
        displayItems.add(item);
    }

    @Override
    public void commonRegister() {
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(title)
                .icon(iconSupplier)
                .displayItems(this::addDisplayItems)
                .build();
        var key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(AE.MOD_ID, id));
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    @Override
    public void close() {
        displayItems = null;
    }

    private void addDisplayItems(
            CreativeModeTab.ItemDisplayParameters p,
            CreativeModeTab.Output out) {
        displayItems.forEach(out::accept);
    }
}
