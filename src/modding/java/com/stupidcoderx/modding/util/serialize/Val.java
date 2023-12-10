package com.stupidcoderx.modding.util.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;

public abstract class Val<T, V extends Val<T,V>> implements Serializable<V> {
    T data;
    ValType type;

    Val(ValType type) {
        this.type = type;
        this.data = type.defaultVal();
    }

    abstract void dataToNetwork(FriendlyByteBuf buf);

    static Val<?,?> parse(JsonElement e) {
        Val<?,?> res;
        if (e instanceof JsonObject) {
            res = new ContainerVal().fromJson(e);
        } else if (e instanceof JsonPrimitive obj) {
            res = parsePrimitive(obj);
        } else if (e instanceof JsonArray) {
            res = new ArrayVal().fromJson(e);
        } else {
            throw new IllegalArgumentException();
        }
        return res;
    }

    static Val<?,?> parsePrimitive(JsonPrimitive json) {
        if (json.isNumber()) {
            return parseNum(json);
        } else if (json.isString()) {
            StringVal res = new StringVal();
            res.set(json.getAsString());
            return res;
        }
        throw new IllegalArgumentException("unexpected primitive type");
    }

    private static NumVal<?,?> parseNum(JsonPrimitive json) {
        Number num = json.getAsNumber();
        NumVal<?,?> res;
        if (num instanceof Integer) {
            res = new IntVal();
        } else {
            throw new IllegalArgumentException("unexpected num type:" + num.getClass());
        }
        res.setNum(num);
        return res;
    }

    static Val<?,?> parse(FriendlyByteBuf buf) {
        return ValType.fromOrdinal(buf.readInt()).valBuilder.get().fromNetWork(buf);
    }

    @Override
    public final void toNetWork(FriendlyByteBuf buf) {
        buf.writeInt(type.ordinal());
        dataToNetwork(buf);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public T get() {
        return data;
    }

    public void set(T data) {
        this.data = data;
    }

    public IntVal asInt() {
        return (IntVal) this;
    }

    public ContainerVal asContainer() {
        return (ContainerVal) this;
    }

    public ArrayVal asArray() {
        return (ArrayVal) this;
    }

    public StringVal asString() {
        return (StringVal) this;
    }
}
