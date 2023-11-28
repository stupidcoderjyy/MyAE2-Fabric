package com.stupidcoderx.modding.datagen.model.elements;

public interface ISeparateOperation {
    void onSeparated(Cube old, SeparationResult<Cube> result);
}
