package com.stupidcoderx.modding.datagen.blockstate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModelList {
    List<Model> models = new ArrayList<>();

    public ModelList create(String prefix, ResourceLocation location, Consumer<Model> op) {
        Model e = new Model(ModelBuilder.expandLoc(prefix, location));
        op.accept(e);
        models.add(e);
        return this;
    }

    public ModelList create(ResourceLocation location, Consumer<Model> op) {
        return create("block", location, op);
    }

    public ModelList create(ResourceLocation location) {
        return create("block", location, m -> {});
    }

    public ModelList create(String prefix, ResourceLocation location) {
        return create(prefix, location, m -> {});
    }

    JsonElement toJson() {
        if (models.size() == 1) {
            return models.get(0).toJson();
        }
        JsonArray arr = new JsonArray();
        models.forEach(m -> arr.add(m.toJson()));
        return arr;
    }
}
