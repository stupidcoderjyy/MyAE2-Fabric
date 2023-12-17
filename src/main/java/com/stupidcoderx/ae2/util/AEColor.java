package com.stupidcoderx.ae2.util;

import com.stupidcoderx.modding.datagen.lang.ILocalizationEnum;

public enum AEColor implements ILocalizationEnum {
    WHITE("白色", "white", 0xDBDBDB),
    ORANGE("橙色","orange",0xFAAE44);

    public final String id;
    public final int rgbNormal;
    public final String defaultName;
    public final String translationKey;

    AEColor(String defaultName, String id, int rgbNormal) {
        this.id = id;
        this.rgbNormal = rgbNormal;
        this.defaultName = defaultName;
        this.translationKey = "color.ae2." + id;
    }

    @Override
    public String defaultName() {
        return defaultName;
    }

    @Override
    public String translationKey() {
        return translationKey;
    }
}
