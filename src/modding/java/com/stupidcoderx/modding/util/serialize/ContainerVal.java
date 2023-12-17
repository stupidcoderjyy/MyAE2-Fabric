package com.stupidcoderx.modding.util.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ContainerVal extends Val<Map<String, Field<?>>, ContainerVal>{
    public ContainerVal() {
        super(ValType.CONTAINER);
    }

    public ContainerVal newContainer(String key) {
        return create(key, new ContainerVal());
    }

    public IntVal newInt(String key) {
        return create(key, new IntVal());
    }

    public ArrayVal newArray(String key) {
        return create(key, new ArrayVal());
    }

    public StringVal newString(String key) {
        return create(key, new StringVal());
    }

    private <T extends Val<?,T>> T create(String key, T val) {
        Field<T> f = new Field<>(key, val);
        data.put(key,f);
        return val;
    }

    public Val<?,?> get(String key) {
        Field<?> f = data.get(key);
        return f == null ? null : f.data;
    }

    @Override
    public JsonElement toJson(@Nullable JsonElement parent) {
        JsonObject obj = new JsonObject();
        data.forEach((k,f) -> f.toJson(obj));
        return obj;
    }

    @Override
    public ContainerVal fromJson(JsonElement json) {
        for (var entry : json.getAsJsonObject().asMap().entrySet()) {
            String key = entry.getKey();
            JsonElement e = entry.getValue();
            data.put(key, new Field<>(key, null).fromJson(e));
        }
        return this;
    }

    @Override
    public void dataToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        data.forEach((k,f) -> f.toNetWork(buf));
    }

    @Override
    public ContainerVal fromNetWork(FriendlyByteBuf buf) {
        data = new HashMap<>();
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            Field<?> field = new Field<>().fromNetWork(buf);
            data.put(field.key, field);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        data.forEach((k,v) -> sb.append(v).append(','));
        if (!data.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
