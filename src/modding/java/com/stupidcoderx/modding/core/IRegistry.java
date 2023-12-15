package com.stupidcoderx.modding.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface IRegistry {
    default void commonRegister() {
    }

    @Environment(EnvType.CLIENT)
    default void clientRegister() {
    }

    default void provideData() {
    }

    default void close() {
    }

    default String debugName() {
        return Mod.getModId() + " -> " + this;
    }
}
