package com.stupidcoderx.modding.datagen.recipe;

import com.google.gson.JsonElement;
import com.stupidcoderx.modding.datagen.ModDataProvider;
import com.stupidcoderx.modding.datagen.ResourceType;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends ModDataProvider<RecipeProvider> {
    private final Map<ResourceLocation, RecipeDef<?>> recipeTypes = new HashMap<>();

    public RecipeProvider() {
        super(ResourceType.RECIPE);
    }

    public <T extends ModRecipe<T>> void registerType(RecipeDef<T> def) {
        recipeTypes.put(def.typeLoc, def);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return getCollectedTask(recipeTypes.entrySet(), typeEntry -> {
            RecipeDef<?> def = typeEntry.getValue();
            return getCollectedTask(def.recipeFactories.entrySet(), recipeEntry -> {
                ModRecipe<?> recipe = def.createFull(recipeEntry.getKey());
                JsonElement json = recipe.root.toJson(null);
                return getJsonWritingTask(recipeEntry.getKey(), json, cachedOutput);
            });
        });
    }

    @Override
    public String getName() {
        return "RecipeProvider";
    }
}
