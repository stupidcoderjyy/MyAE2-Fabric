package com.stupidcoderx.modding.datagen.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ModelTransform {
    final String type;
    private final float[] rotation = new float[3];
    private final float[] translation = new float[3];
    private final float[] scale = new float[3];

    private ModelTransform(String type) {
        this.type = type;
    }

    public ModelTransform rotation(float x, float y, float z) {
        rotation[0] = x;
        rotation[1] = y;
        rotation[2] = z;
        return this;
    }
    
    public ModelTransform translation(float x, float y, float z) {
        translation[0] = x;
        translation[1] = y;
        translation[2] = z;
        return this;
    }

    public ModelTransform scale(float x, float y, float z) {
        scale[0] = x;
        scale[1] = y;
        scale[2] = z;
        return this;
    }

    public ModelTransform scale(float ratio) {
        return scale(ratio, ratio, ratio);
    }

    void toJson(JsonObject parent) {
        JsonObject obj = new JsonObject();
        obj.add("rotation", new Gson().toJsonTree(rotation));
        obj.add("translation", new Gson().toJsonTree(translation));
        obj.add("scale", new Gson().toJsonTree(scale));
        parent.add(type, obj);
    }

    public static ModelTransform gui() {
        return new ModelTransform("gui");
    }

    public static ModelTransform fixed() {
        return new ModelTransform("fixed");
    }

    public static ModelTransform rightHandThirdPerson() {
        return new ModelTransform("thirdperson_righthand");
    }

    public static ModelTransform rightHandFirstPerson() {
        return new ModelTransform("firstperson_righthand");
    }

    public static ModelTransform leftHandFirstPerson() {
        return new ModelTransform("firstperson_lefthand");
    }
}
