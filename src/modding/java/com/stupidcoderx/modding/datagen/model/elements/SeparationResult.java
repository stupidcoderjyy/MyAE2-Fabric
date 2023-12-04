package com.stupidcoderx.modding.datagen.model.elements;

import java.util.EnumMap;
import java.util.Map;

public class SeparationResult<T extends SeparateObject<T>> {
    public final Map<Direction, T> children = new EnumMap<>(Direction.class);

    SeparationResult(){
    }
}
