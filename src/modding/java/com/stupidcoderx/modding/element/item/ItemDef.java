package com.stupidcoderx.modding.element.item;

import com.stupidcoderx.modding.core.DataGenOnly;
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

import java.util.Stack;
import java.util.function.Function;

/**
 * 模组物品容器，负责物品的各种注册、数据生成和其他操作。
 * @param <I> 物品类
 */
public class ItemDef<I extends Item> implements IRegistry, ItemLike {
    private static final Stack<Object> TABS = new Stack<>();
    public final I item;
    public final ResourceLocation loc;
    public final String defaultName;

    public ItemDef(ResourceLocation loc, String name, I item) {
        this.item = item;
        this.loc = loc;
        this.defaultName = name;
        Mod.ITEM_LIKE_REGISTRY.add(this);
        TABS.forEach(this::handleTab);
    }

    public ItemDef(String id, String name, I item) {
        this(Mod.modLoc(id), name, item);
    }

    private void handleTab(Object o) {
        if (o instanceof ModCreativeTab tab) {
            tab.add(this);
        } else {
            ItemGroupEvents.modifyEntriesEvent((ResourceKey<CreativeModeTab>) o)
                    .register(entries -> entries.accept(this));
        }
    }

    /**
     * 创建一个默认的物品
     * @param id 物品id
     * @param name 物品默认本地化名称
     * @return 物品容器
     */
    public static ItemDef<Item> simple(String id, String name) {
        return new ItemDef<>(id, name, new Item(new Item.Properties()));
    }

    /**
     * 创建一个模型继承于"minecraft:item/handheld"，且最大堆叠数为1的物品
     * @param id 物品id
     * @param name 物品默认本地化名称
     * @param factory 物品构造器
     * @return 物品容器
     * @param <T> 工具类
     */
    public static <T extends Item> ItemDef<T> tool(String id, String name, Function<Item.Properties, T> factory) {
        return withModelPrefix(Mod.modLoc(id), name, factory.apply(new Item.Properties().stacksTo(1)), "handheld");
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
    @DataGenOnly
    public void provideData() {
        provideModel();
        DataProviders.LOCALIZATION.register(getTranslationKey(), getDefaultName());
    }

    /**
     * 数据生成环境下的物品模型注册逻辑
     */
    @DataGenOnly
    protected void provideModel() {
        DataProviders.MODEL_ITEM.model(loc)
                .parent("minecraft:item/generated")
                .texture("layer0", loc);
    }

    /**
     * 数据生成环境下，获取物品本地化名称的键
     */
    @DataGenOnly
    protected String getTranslationKey() {
        return "item." + Mod.id() + "." + loc.getPath();
    }

    /**
     *
     * @return 数据生成环境下，获取物品默认的本地化名称
     */
    @DataGenOnly
    protected String getDefaultName() {
        return defaultName;
    }

    /**
     * 向栈中压入一个创造模式物品栏。在ItemDef初始化过程中，物品会被加入栈中的所有创造模式物品栏中
     * @param tab 原版创造模式物品栏
     */
    public static void pushTab(ResourceKey<CreativeModeTab> tab) {
        TABS.push(tab);
    }

    /**
     * 向栈中压入一个创造模式物品栏。在ItemDef初始化过程中，物品会被加入栈中的所有创造模式物品栏中
     * @param tab 模组创造模式物品栏
     */
    public static void pushTab(ModCreativeTab tab) {
        TABS.push(tab);
    }

    /**
     * 从栈中弹出一个创造模式物品栏
     */
    public static void popTab() {
        TABS.pop();
    }

    private static <T extends Item> ItemDef<T> withModelPrefix(ResourceLocation loc, String name, T item, String prefix) {
        return new ItemDef<>(loc, name, item) {
            @Override
            protected void provideModel() {
                DataProviders.MODEL_ITEM.model(loc)
                        .parent("minecraft:item/" + prefix)
                        .texture("layer0", loc);
            }
        };
    }
}
