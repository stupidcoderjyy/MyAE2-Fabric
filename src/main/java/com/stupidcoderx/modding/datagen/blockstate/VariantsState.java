package com.stupidcoderx.modding.datagen.blockstate;

import com.google.common.base.Preconditions;

public class VariantsState {
    final String[] properties;
    private final VariantsBlockStateBuilder parent;

    VariantsState(VariantsBlockStateBuilder parent, String[] properties) {
        this.properties = properties;
        this.parent = parent;
    }

    public String getVal(String key) {
        VariantType type = parent.nameToTypes.get(key);
        return properties[type.id];
    }

    public String getVal(int typeId) {
        Preconditions.checkElementIndex(typeId, properties.length);
        return properties[typeId];
    }

    @Override
    public String toString() {
        if (properties.length == 0) {
            return "";
        }
        if (properties.length == 1) {
            return parent.idToTypes.get(0).key + "=" + properties[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < properties.length - 1; i++) {
            property(sb, i);
            sb.append(',');
        }
        property(sb, properties.length - 1);
        return sb.toString();
    }

    private void property(StringBuilder sb, int i) {
        sb.append(parent.idToTypes.get(i).key).append('=').append(properties[i]);
    }
}
