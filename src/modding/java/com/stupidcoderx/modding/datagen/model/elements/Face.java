package com.stupidcoderx.modding.datagen.model.elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class Face {
    final Direction direction;
    final float[] uv;
    String texture;
    int rotateTimes = 0;

    Face(Direction direction) {
        this.direction = direction;
        this.uv = new float[4];
    }

    void uv(float x1, float y1, float x2, float y2) {
        uv[0] = x1;
        uv[1] = y1;
        uv[2] = x2;
        uv[3] = y2;
    }

    void texture(String t) {
        this.texture = t;
    }

    void shrink(boolean startVertex, int dim, float val) {
        if (startVertex) {
            uv[dim] += val;
        } else {
            uv[dim + 2] -= val;
        }
    }

    Face copy() {
        Face f = new Face(direction);
        System.arraycopy(uv, 0, f.uv, 0, 4);
        f.texture = texture;
        return f;
    }

    JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("uv", new Gson().toJsonTree(uv));
        json.addProperty("texture", texture);
        if (rotateTimes % 4 != 0) {
            json.addProperty("rotation", (rotateTimes % 4) * 90);
        }
        return json;
    }

    @Override
    public String toString() {
        return Arrays.toString(uv);
    }
}
