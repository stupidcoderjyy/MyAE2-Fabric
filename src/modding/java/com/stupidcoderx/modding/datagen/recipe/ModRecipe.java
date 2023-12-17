package com.stupidcoderx.modding.datagen.recipe;

import com.stupidcoderx.modding.util.serialize.ContainerVal;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * 模组配方，需要实现两个功能：
 * <p>1）数据生成阶段的配方构造（对{@link ContainerVal}进行构造），用于生成json文件；
 * <p>2）游戏加载阶段从{@link ContainerVal}解析配方数据的逻辑
 * @param <R> 配方子类，便于链式调用
 */
public abstract class ModRecipe<R extends ModRecipe<R>> implements Recipe<Container> {
    protected final ContainerVal root = new ContainerVal();
    private final RecipeDef<R> def;
    private final ResourceLocation loc;

    public ModRecipe(ResourceLocation loc, RecipeDef<R> def) {
        this.def = def;
        this.loc = loc;
        root.newString("type").set(def.typeLoc);
    }

    /**
     * 完成反序列化之后调用此方法，用于设置配方相关的数据
     */
    protected abstract void onDeserialized();

    /**
     * 检查容器内的信息是否符合当前配方（一般只有涉及容器的合成才有用）
     * @param container 目标容器
     * @param level 世界
     * @return 是否符合
     */
    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    /**
     * 根据配方对容器内的某些元素进行合成，获得配方结果（一般只有涉及容器的合成才有用）
     * @param container 发生合成的容器
     * @param registryAccess 顶层注册系统
     * @return 配方结果
     */
    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    /**
     * 是否能在特定大小的网格内合成物品（一般只有合成配方才用的到）
     * @param columns 网格列数
     * @param rows 网格行数
     * @return 能合成返回true
     */
    @Override
    public boolean canCraftInDimensions(int columns, int rows) {
        return false;
    }

    /**
     * 获得配方结果（只有结果为物品时才用的到）
     * @param registryAccess 顶层注册系统，暂不清楚这里有什么用
     * @return 配方结果
     */
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public final RecipeSerializer<?> getSerializer() {
        return def;
    }

    @Override
    public final RecipeType<?> getType() {
        return def.type;
    }

    @Override
    public final ResourceLocation getId() {
        return loc;
    }
}
