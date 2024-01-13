package com.stupidcoderx.modding.datagen.blockstate.variants;

import com.google.common.base.Preconditions;

public class VariantsState {
    final String[] properties;
    final int[] optionIndex;
    private final VariantsBlockStateBuilder parent;

    VariantsState(VariantsBlockStateBuilder parent, String[] properties, int[] optionIndex) {
        this.properties = properties;
        this.parent = parent;
        this.optionIndex = optionIndex;
    }

    /**
     * 根据变量名获取当前值
     * @param key 变量名
     * @return 当前值
     */
    public String option(String key) {
        VariantType type = parent.nameToTypes.get(key);
        return properties[type.id];
    }

    /**
     * 根据变量id获取当前值
     * @param typeId 变量id，由变量添加顺序决定
     * @return 当前的值
     */
    public String option(int typeId) {
        Preconditions.checkElementIndex(typeId, properties.length);
        return properties[typeId];
    }

    /**
     * 根据变量id获取当前值在选项列表中的索引
     * @param typeId 变量id，由变量添加顺序决定
     * @return 当前值在选项列表中的索引
     */
    public int optionIndex(int typeId) {
        Preconditions.checkElementIndex(typeId, properties.length);
        return optionIndex[typeId];
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
