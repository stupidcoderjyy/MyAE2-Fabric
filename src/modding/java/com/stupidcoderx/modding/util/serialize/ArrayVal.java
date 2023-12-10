package com.stupidcoderx.modding.util.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArrayVal extends Val<List<Val<?,?>>, ArrayVal>{
    ArrayVal() {
        super(ValType.ARRAY);
    }

    @Override
    public JsonElement toJson(@Nullable JsonElement parent) {
        JsonArray json = new JsonArray();
        data.forEach(v -> json.add(v.toJson(null)));
        return json;
    }

    @Override
    public ArrayVal fromJson(JsonElement json) {
        json.getAsJsonArray().forEach(e -> data.add(Val.parse(e)));
        return this;
    }

    @Override
    void dataToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        data.forEach(v -> v.toNetWork(buf));
    }

    @Override
    public ArrayVal fromNetWork(FriendlyByteBuf buf) {
        int size = buf.readInt();
        data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(Val.parse(buf));
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        data.forEach(v -> sb.append(v).append(','));
        if (!data.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    public IntVal addInt() {
        return add(new IntVal());
    }

    public ContainerVal addContainer() {
        return add(new ContainerVal());
    }

    public ArrayVal addArray() {
        return add(new ArrayVal());
    }

    public StringVal addString() {
        return add(new StringVal());
    }

    private <T extends Val<?,T>> T add(T val) {
        data.add(val);
        return val;
    }

    public Val<?,?> get(int i) {
        return data.get(i);
    }
}
