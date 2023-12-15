package com.stupidcoderx.modding.datagen.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Display {
    final String type;
    private final float[] rotation = new float[3];
    private final float[] translation = new float[3];
    private final float[] scale = new float[3];

    private Display(String type) {
        this.type = type;
    }

    public Display rotation(float x, float y, float z) {
        rotation[0] = x;
        rotation[1] = y;
        rotation[2] = z;
        return this;
    }
    
    public Display translation(float x, float y, float z) {
        translation[0] = x;
        translation[1] = y;
        translation[2] = z;
        return this;
    }

    public Display scale(float x, float y, float z) {
        scale[0] = x;
        scale[1] = y;
        scale[2] = z;
        return this;
    }

    public Display scale(float ratio) {
        return scale(ratio, ratio, ratio);
    }

    void toJson(JsonObject parent) {
        JsonObject obj = new JsonObject();
        obj.add("rotation", new Gson().toJsonTree(rotation));
        obj.add("translation", new Gson().toJsonTree(translation));
        obj.add("scale", new Gson().toJsonTree(scale));
        parent.add(type, obj);
    }

    public static Display gui() {
        return new Display("gui");
    }

    public static Display fixed() {
        return new Display("fixed");
    }

    public static Display rightHandThirdPerson() {
        return new Display("thirdperson_righthand");
    }

    public static Display rightHandFirstPerson() {
        return new Display("firstperson_righthand");
    }

    public static Display leftHandFirstPerson() {
        return new Display("firstperson_lefthand");
    }
}
