package com.stupidcoderx.modding.util.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StringVal extends Val<String, StringVal>{
    StringVal() {
        super(ValType.STRING);
    }

    @Override
    public JsonElement toJson(@Nullable JsonElement parent) {
        return new JsonPrimitive(data);
    }

    @Override
    public StringVal fromJson(JsonElement json) {
        data = json.getAsJsonPrimitive().getAsString();
        return this;
    }

    @Override
    public StringVal fromNetWork(FriendlyByteBuf buf) {
        data = buf.readUtf();
        return this;
    }

    @Override
    void dataToNetwork(FriendlyByteBuf buf) {
        buf.writeUtf(data);
    }

    public void set(ResourceLocation loc) {
        data = loc.toString();
    }
}
