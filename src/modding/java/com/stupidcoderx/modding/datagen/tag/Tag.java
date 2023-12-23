package com.stupidcoderx.modding.datagen.tag;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public abstract class Tag<T extends Tag<T,E>, E> {
    final List<E> elements = new ArrayList<>();
    boolean replace;

    public T add(E e) {
        elements.add(e);
        return (T)this;
    }

    public T replaceVanilla(boolean replace) {
        this.replace = replace;
        return (T)this;
    }

    abstract JsonElement serialize(E e);

    JsonObject toJson() {
        JsonObject obj = new JsonObject();
        JsonArray arr = new JsonArray();
        obj.add("replace", new JsonPrimitive(replace));
        obj.add("values", arr);
        elements.forEach(e -> arr.add(serialize(e)));
        return obj;
    }
}
