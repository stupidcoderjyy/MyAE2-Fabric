package com.stupidcoderx.modding.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * 模组注册单元，在模组初始化阶段会被调用，执行相关操作
 */
public interface IRegistry {
    /**
     * 在所有环境下都会执行的注册逻辑
     */
    default void commonRegister() {
    }

    /**
     * 仅在客户端环境下执行的注册逻辑
     */
    @Environment(EnvType.CLIENT)
    default void clientRegister() {
    }

    /**
     * 仅在数据生成环境下执行的注册逻辑
     */
    @DataGenOnly
    default void provideData() {
    }

    /**
     * 关闭注册单元
     */
    default void close() {
    }

    default String debugName() {
        return Mod.id() + " -> " + this;
    }
}
