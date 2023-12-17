package com.stupidcoderx.modding.datagen.recipe;

import com.google.gson.JsonObject;
import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * 配方容器，负责自定义配方的序列化、反序列化、注册、数据生成、游戏内获取
 * @param <R> 配方子类
 * @see ModRecipe
 */
public final class RecipeDef<R extends ModRecipe<R>> implements IRegistry, RecipeSerializer<R> {
    final RecipeType<R> type;
    final ResourceLocation typeLoc;
    @DataGenOnly
    final Map<ResourceLocation, Consumer<R>> recipeFactories;
    final BiFunction<ResourceLocation, RecipeDef<R>, R> emptyRecipeSupplier;

    /**
     * 构造器
     * @param typeId 配方类型的id
     * @param emptyRecipeSupplier 空白配方构造器，创建一个没有任何数据的配方
     */
    public RecipeDef(String typeId, BiFunction<ResourceLocation, RecipeDef<R>, R> emptyRecipeSupplier) {
        this.typeLoc = Mod.modLoc(typeId);
        this.type = new RecipeType<>() {
            @Override
            public String toString() {
                return typeId;
            }
        };
        recipeFactories = Mod.isEnvDataGen ? new HashMap<>() : null;
        this.emptyRecipeSupplier = emptyRecipeSupplier;
        Mod.RECIPE_REGISTRY.add(this);
    }

    /**
     * 数据生成阶段构造配方对象，这个配方最终会序列化成json文件
     * @param recipeId 配方id，仅用于文件名
     * @param builder 配方构造器，构造的具体方法需要在{@link ModRecipe}的子类中自己实现
     * @return 调用者
     */
    @DataGenOnly
    public RecipeDef<R> register(String recipeId, Consumer<R> builder) {
        if (Mod.isEnvDataGen) {
            recipeFactories.put(Mod.modLoc(typeLoc.getPath() + "/" + recipeId), builder);
        }
        return this;
    }

    /**
     * 从MC中获取本容器对应的所有配方。
     * <p>此方法需要对{@link net.minecraft.world.item.crafting.RecipeManager#byType(RecipeType)}的访问权限进行修改
     * @param world 任何世界
     * @return 所有配方
     */
    public Map<ResourceLocation, R> getRegisteredRecipes(Level world) {
        return world.getRecipeManager().byType(type);
    }

    @Override
    public void commonRegister() {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, typeLoc, type);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, typeLoc, this);
    }

    @Override
    public void provideData() {
        DataProviders.RECIPE.registerType(this);
    }

    @Override
    public R fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
        R r = createEmpty(loc);
        r.root.fromNetWork(buf);
        r.onDeserialized();
        return r;
    }

    @Override
    public R fromJson(ResourceLocation loc, JsonObject root) {
        R r = createEmpty(loc);
        r.root.fromJson(root);
        r.onDeserialized();
        return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, R recipe) {
        recipe.root.toNetWork(buf);
    }

    R createEmpty(ResourceLocation loc) {
        return emptyRecipeSupplier.apply(loc, this);
    }

    @DataGenOnly
    R createFull(ResourceLocation loc) {
        R e = emptyRecipeSupplier.apply(loc, this);
        recipeFactories.get(loc).accept(e);
        return e;
    }

    @Override
    public String toString() {
        return typeLoc.toString();
    }
}
