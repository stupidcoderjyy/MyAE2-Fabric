package com.stupidcoderx.common.core;

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
