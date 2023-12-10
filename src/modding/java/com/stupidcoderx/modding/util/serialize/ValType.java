package com.stupidcoderx.modding.util.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

enum ValType {
    INT(IntVal::new, () -> 0),
    CONTAINER(ContainerVal::new, HashMap::new),
    ARRAY(ArrayVal::new, ArrayList::new),
    FIELD(Field::new,  () -> null),
    STRING(StringVal::new,  () -> "")
    ;
    final Supplier<Val<?,?>> valBuilder;
    private final Supplier<Object> defaultVal;

    ValType(Supplier<Val<?, ?>> valBuilder, Supplier<Object> defaultVal) {
        this.valBuilder = valBuilder;
        this.defaultVal = defaultVal;
    }

    static ValType fromOrdinal(int id) {
        return values()[id];
    }

    <T> T defaultVal() {
        return (T)defaultVal.get();
    }
}
