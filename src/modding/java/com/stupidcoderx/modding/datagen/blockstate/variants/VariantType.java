package com.stupidcoderx.modding.datagen.blockstate.variants;

import java.util.List;

public class VariantType {
    final int id;
    final String key;
    final List<String> options;

    VariantType(int id, String key, List<String> options) {
        this.id = id;
        this.key = key;
        this.options = options;
    }
}
