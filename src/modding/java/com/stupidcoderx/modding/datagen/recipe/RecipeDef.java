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

public final class RecipeDef<R extends ModRecipe<R>> implements IRegistry, RecipeSerializer<R> {
    final RecipeType<R> type;
    final ResourceLocation typeLoc;
    @DataGenOnly
    final Map<ResourceLocation, Consumer<R>> recipeFactories;
    final BiFunction<ResourceLocation, RecipeDef<R>, R> emptyRecipeSupplier;

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

    @DataGenOnly
    public RecipeDef<R> register(String recipeId, Consumer<R> builder) {
        if (Mod.isEnvDataGen) {
            recipeFactories.put(Mod.modLoc(typeLoc.getPath() + "/" + recipeId), builder);
        }
        return this;
    }

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
