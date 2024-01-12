package com.stupidcoderx.modding.datagen.blockstate;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.stupidcoderx.modding.core.Mod;
import net.minecraft.resources.ResourceLocation;

public class Model {
    private final ResourceLocation path;
    private final int[] rotation = new int[3];
    private boolean uvLock;
    private static final String[] rotationAxis = {"x", "y", "z"};

    public Model(ResourceLocation path) {
        this.path = Mod.expandLoc("block", path);
    }

    public Model(String path) {
        this.path = Mod.expandLoc("block", Mod.modLoc(path));
    }

    public Model rotationX(int times) {
        rotation[0] = (times % 4) * 90;
        return this;
    }

    public Model rotationY(int times) {
        rotation[1] = (times % 4) * 90;
        return this;
    }

    public Model rotationZ(int times) {
        rotation[2] = (times % 4) * 90;
        return this;
    }

    public Model uvLock(boolean uvLock) {
        this.uvLock = uvLock;
        return this;
    }

    public JsonObject toJson() {
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("model", path.toString());
        for (int i = 0 ; i < 3 ; i ++) {
            if (rotation[i] != 0) {
                modelObj.addProperty(rotationAxis[i], rotation[i]);
            }
        }
        if (uvLock) {
            modelObj.add("uvLock", new JsonPrimitive(true));
        }
        return modelObj;
    }
}
