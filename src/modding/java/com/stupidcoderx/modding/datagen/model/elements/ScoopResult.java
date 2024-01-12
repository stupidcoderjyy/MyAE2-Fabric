package com.stupidcoderx.modding.datagen.model.elements;

import java.util.EnumMap;
import java.util.Map;

public class ScoopResult {
    public final Map<Direction, Cube> children = new EnumMap<>(Direction.class);

    ScoopResult(){
    }
}
