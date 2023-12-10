package com.stupidcoderx.modding.util.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public abstract class NumVal<N extends Number,V extends NumVal<N,V>> extends Val<N, V>{
    NumVal(ValType type) {
        super(type);
    }

    abstract void setNum(Number num);

    @Override
    public JsonElement toJson(@Nullable JsonElement parent) {
        return new JsonPrimitive(data);
    }
}
