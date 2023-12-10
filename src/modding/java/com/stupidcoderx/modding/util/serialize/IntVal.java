package com.stupidcoderx.modding.util.serialize;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;

public class IntVal extends NumVal<Integer, IntVal> {
    protected IntVal() {
        super(ValType.INT);
    }

    @Override
    public IntVal fromJson(JsonElement json) {
        data = json.getAsJsonPrimitive().getAsInt();
        return this;
    }

    @Override
    public void dataToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(data);
    }

    @Override
    public IntVal fromNetWork(FriendlyByteBuf buf) {
        data = buf.readInt();
        return this;
    }

    @Override
    void setNum(Number num) {
        data = num.intValue();
    }
}
