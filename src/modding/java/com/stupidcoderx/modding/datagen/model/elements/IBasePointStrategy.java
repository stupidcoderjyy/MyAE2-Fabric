package com.stupidcoderx.modding.datagen.model.elements;

public interface IBasePointStrategy {
    void setBasePoint(float[] base, float xLen, float yLen, float zLen);

    IBasePointStrategy CENTER = (base, xLen, yLen, zLen) -> {
        base[0] = xLen / 2;
        base[1] = yLen / 2;
        base[2] = zLen / 2;
    };

    IBasePointStrategy NX = (base, xLen, yLen, zLen) -> {
        base[0] = 0;
        base[1] = yLen / 2;
        base[2] = zLen / 2;
    };

    IBasePointStrategy NY = (base, xLen, yLen, zLen) -> {
        base[0] = xLen / 2;
        base[1] = 0;
        base[2] = zLen / 2;
    };

    IBasePointStrategy NZ = (base, xLen, yLen, zLen) -> {
        base[0] = xLen / 2;
        base[1] = yLen / 2;
        base[2] = 0;
    };

    IBasePointStrategy PX = (base, xLen, yLen, zLen) -> {
        base[0] = xLen;
        base[1] = yLen / 2;
        base[2] = zLen / 2;
    };

    IBasePointStrategy PY = (base, xLen, yLen, zLen) -> {
        base[0] = xLen / 2;
        base[1] = yLen;
        base[2] = zLen / 2;
    };

    IBasePointStrategy PZ = (base, xLen, yLen, zLen) -> {
        base[0] = xLen / 2;
        base[1] = yLen / 2;
        base[2] = zLen;
    };

    IBasePointStrategy N_CORNER = (base, xLen, yLen, zLen) -> {
        base[0] = 0;
        base[1] = 0;
        base[2] = 0;
    };

    IBasePointStrategy P_CORNER = (base, xLen, yLen, zLen) -> {
        base[0] = xLen;
        base[1] = yLen;
        base[2] = zLen;
    };
}
