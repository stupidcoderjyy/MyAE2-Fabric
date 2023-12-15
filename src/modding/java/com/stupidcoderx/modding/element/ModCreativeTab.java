package com.stupidcoderx.modding.element;

import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTab implements IRegistry {
    private List<ItemLike> displayItems = new ArrayList<>();
    private final Supplier<ItemLike> iconSupplier;
    private final Component title;
    private final String id;

    public ModCreativeTab(Supplier<ItemLike> icon, String id, Component title) {
        this.iconSupplier = icon;
        this.title = title;
        this.id = id;
        Mod.CREATIVE_TAB_REGISTRY.add(this);
    }

    public ModCreativeTab(Supplier<ItemLike> icon, String id) {
        this(icon, id, Component.translatable("tab." + Mod.getModId() + "." + id));
    }

    public void add(ItemLike item) {
        displayItems.add(item);
    }

    @Override
    public void commonRegister() {
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(title)
                .icon(() -> new ItemStack(iconSupplier.get()))
                .displayItems((p, out) -> displayItems.forEach(out::accept))
                .build();
        var key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Mod.modLoc(id));
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    @Override
    public void close() {
        displayItems = null;
    }
}
