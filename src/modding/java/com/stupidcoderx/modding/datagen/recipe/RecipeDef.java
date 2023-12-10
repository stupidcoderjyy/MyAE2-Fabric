package com.stupidcoderx.modding.datagen.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class RecipeDef<R extends ModRecipe<R>> implements IRegistry, RecipeSerializer<R> {
    final RecipeType<R> type;
    final ResourceLocation typeLoc;
    final Map<ResourceLocation, Consumer<R>> recipeFactories = new HashMap<>();
    final Function<RecipeDef<R>, R> emptyRecipeSupplier;
    private final Codec<R> codec = ExtraCodecs.adaptJsonSerializer(this::fromJson, this::toJson);

    public RecipeDef(String typeId, Function<RecipeDef<R>, R> emptyRecipeSupplier) {
        this.typeLoc = Mod.modLoc(typeId);
        this.type = new RecipeType<>() {
            @Override
            public String toString() {
                return typeId;
            }
        };
        this.emptyRecipeSupplier = emptyRecipeSupplier;
        Mod.RECIPE_REGISTRY.add(this);
    }

    public RecipeDef<R> register(String recipeId, Consumer<R> builder) {
        recipeFactories.put(Mod.modLoc(typeLoc.getPath() + "/" + recipeId), builder);
        return this;
    }

    @Override
    public void commonRegister() {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, typeLoc, type);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, typeLoc, this);
    }

    @Override
    public void buildData() {
        DataProviders.RECIPE.registerType(this);
    }

    private JsonElement toJson(R recipe) {
        return recipe.root.toJson(null);
    }

    private R fromJson(JsonElement root) {
        R r = createEmpty();
        r.root.fromJson(root);
        r.onDeserialized();
        return r;
    }

    @Override
    public Codec<R> codec() {
        return codec;
    }

    @Override
    public R fromNetwork(FriendlyByteBuf buf) {
        R r = createEmpty();
        r.root.fromNetWork(buf);
        r.onDeserialized();
        return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, R recipe) {
        recipe.root.toNetWork(buf);
    }

    R createEmpty() {
        return emptyRecipeSupplier.apply(this);
    }

    R createFull(ResourceLocation loc) {
        R e = emptyRecipeSupplier.apply(this);
        recipeFactories.get(loc).accept(e);
        return e;
    }

    @Override
    public String toString() {
        return typeLoc.toString();
    }
}
