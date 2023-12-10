package com.stupidcoderx.modding.util.serialize;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class Field<V extends Val<?, V>> extends Val<V, Field<V>>{
    String key;

    Field(String key, V val) {
        super(ValType.FIELD);
        this.key = key;
        this.data = val;
    }

    Field() {
        this(null, null);
    }

    @Override
    public JsonElement toJson(@Nullable JsonElement parent) {
        Preconditions.checkArgument(parent instanceof JsonObject, "requires JsonObject");
        Preconditions.checkNotNull(key, "null key");
        parent.getAsJsonObject().add(key, data.toJson(null));
        return parent;
    }

    @Override
    public Field<V> fromJson(JsonElement json) {
        Preconditions.checkNotNull(key, "null key");
        data = (V) parse(json);
        return this;
    }

    @Override
    public void dataToNetwork(FriendlyByteBuf buf) {
        super.toNetWork(buf);
        Preconditions.checkNotNull(key, "null key");
        buf.writeUtf(key);
        data.toNetWork(buf);
    }

    @Override
    public Field<V> fromNetWork(FriendlyByteBuf buf) {
        key = buf.readUtf();
        data = (V) parse(buf);
        return this;
    }

    @Override
    public String toString() {
        return key + ":" + data;
    }

    public String key() {
        return key;
    }
}
