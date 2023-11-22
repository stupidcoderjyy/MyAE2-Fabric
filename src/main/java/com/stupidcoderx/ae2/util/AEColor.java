package com.stupidcoderx.ae2.util;

public enum AEColor {
    WHITE("white", 0xDBDBDB),
    ORANGE("orange",0xFAAE44);

    public final String id;
    public final int rgbNormal;

    AEColor(String id, int rgbNormal) {
        this.id = id;
        this.rgbNormal = rgbNormal;
    }
}
