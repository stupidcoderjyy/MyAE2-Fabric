package com.stupidcoderx.modding.datagen.blockstate.multipart;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stupidcoderx.modding.datagen.blockstate.IBuilderElement;
import com.stupidcoderx.modding.datagen.blockstate.Model;

import java.util.ArrayList;
import java.util.List;

public class MultiPartBlockStateBuilder implements IBuilderElement {
    private final List<ModelCondition> conditions = new ArrayList<>();
    private Model model;

    public MultiPartBlockStateBuilder apply(Model model) {
        this.model = model;
        return this;
    }

    public void when(ModelPredicate list) {
        Preconditions.checkState(model != null, "set model first!");
        conditions.add(new ModelCondition(model, list));
    }

    public void when(String key, String ... options) {
        when(new ListPredicate().add(key, options));
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonArray condArr = new JsonArray();
        for (ModelCondition c : conditions) {
            JsonObject obj = new JsonObject();
            obj.add("apply", c.model.toJson());
            obj.add("when", c.predicate.toJson());
            condArr.add(obj);
        }
        root.add("multipart", condArr);
        return root;
    }

    private static class ModelCondition{
        Model model;
        ModelPredicate predicate;

        ModelCondition(Model model, ModelPredicate predicate) {
            this.model = model;
            this.predicate = predicate;
        }
    }
}
