package com.stupidcoderx.modding.datagen.blockstate;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class Model {
    private final ResourceLocation path;
    private final int[] rotation = new int[3];
    private static final String[] rotationAxis = {"x", "y", "z"};

    Model(ResourceLocation path) {
        this.path = path;
    }

    public Model rotation(int axis, int val) {
        Preconditions.checkArgument(val == 0 || val == 90 || val == 180 || val == 270,
                "invalid rotation:" + val);
        Preconditions.checkArgument(axis >= 0 && axis <= 3, "invalid axis:" + axis);
        rotation[axis] = val;
        return this;
    }

    JsonObject toJson() {
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("model", path.toString());
        for (int i = 0 ; i < 3 ; i ++) {
            if (rotation[i] != 0) {
                modelObj.addProperty(rotationAxis[i], rotation[i]);
            }
        }
        return modelObj;
    }
}
