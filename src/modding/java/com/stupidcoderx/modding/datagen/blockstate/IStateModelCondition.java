package com.stupidcoderx.modding.datagen.blockstate;

@FunctionalInterface
public interface IStateModelCondition {
    /**
     * 获取每个方块状态对应的模型
     * @param state 方块状态
     * @return 模型列表
     * @see ModelList
     * @see VariantsState
     */
    ModelList getModel(VariantsState state);
}
