package com.stupidcoderx.modding.datagen.blockstate;

@FunctionalInterface
public interface IStateModelCondition {
    ModelList getModel(VariantsState state);
}
