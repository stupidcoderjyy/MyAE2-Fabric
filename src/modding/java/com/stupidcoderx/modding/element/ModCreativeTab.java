package com.stupidcoderx.modding.element;

import com.google.common.base.Preconditions;
import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
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

/**
 * 模组创造模式物品栏，创建对象即可完成注册
 */
public class ModCreativeTab implements IRegistry {
    private List<ItemLike> displayItems = new ArrayList<>();
    private final ResourceKey<CreativeModeTab> key;
    private final Supplier<ItemLike> iconSupplier;
    private final String defaultName;
    private final String translationKey;

    /**
     * 构建并注册一个创造模式物品栏
     * @param id 物品栏id
     * @param defaultName 默认名称
     * @param icon 物品栏的图标
     */
    public ModCreativeTab(String id, String defaultName, Supplier<ItemLike> icon) {
        this.iconSupplier = icon;
        this.translationKey = "tab." + Mod.id() + "." + id;
        this.defaultName = defaultName;
        this.key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Mod.modLoc(id));
        Mod.CREATIVE_TAB_REGISTRY.add(this);
    }

    /**
     * 向物品栏中添加物品或者方块
     * @param item 物品
     */
    public void add(ItemLike item) {
        Preconditions.checkState(displayItems != null, "closed");
        displayItems.add(item);
    }

    @Override
    public void commonRegister() {
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable(translationKey))
                .icon(() -> new ItemStack(iconSupplier.get()))
                .displayItems((p, out) -> displayItems.forEach(out::accept))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    @Override
    public void provideData() {
        DataProviders.LOCALIZATION.register(translationKey, defaultName);
    }

    @Override
    public void close() {
        displayItems = null;
    }
}
