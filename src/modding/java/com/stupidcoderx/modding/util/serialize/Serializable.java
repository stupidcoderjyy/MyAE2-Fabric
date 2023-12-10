package com.stupidcoderx.modding.util.serialize;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface Serializable<T> {
    JsonElement toJson(@Nullable JsonElement parent);
    T fromJson(JsonElement json);
    void toNetWork(FriendlyByteBuf buf);
    T fromNetWork(FriendlyByteBuf buf);
}
