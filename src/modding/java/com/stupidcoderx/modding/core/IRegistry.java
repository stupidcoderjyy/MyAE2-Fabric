package com.stupidcoderx.modding.core;

public interface IRegistry {
    default void commonRegister() {
    }

    default void buildData() {
    }

    default void close() {
    }

    default String debugName() {
        return Mod.getModId() + " -> " + this;
    }
}
