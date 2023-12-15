package com.stupidcoderx.modding.datagen.model.elements;

public interface ICubeCreateStrategy {
    void set(float[] space, float[] base, float xLen, float yLen, float zLen);
    
    ICubeCreateStrategy CENTER = (space, base, xLen, yLen, zLen) -> {
        float offX = xLen / 2, offY = yLen / 2, offZ = zLen / 2;
        space[0] = base[0] - offX;
        space[1] = base[1] - offY;
        space[2] = base[2] - offZ;
        space[3] = base[0] + offX;
        space[4] = base[1] + offY;
        space[5] = base[2] + offZ;
    };

    ICubeCreateStrategy NX = (space, base, xLen, yLen, zLen) -> {
        float offY = yLen / 2, offZ = zLen / 2;
        space[0] = base[0];
        space[1] = base[1] - offY;
        space[2] = base[2] - offZ;
        space[3] = base[0] + xLen;
        space[4] = base[1] + offY;
        space[5] = base[2] + offZ;
    };

    ICubeCreateStrategy NY = (space, base, xLen, yLen, zLen) -> {
        float offX = xLen / 2, offZ = zLen / 2;
        space[0] = base[0] - offX;
        space[1] = base[1];
        space[2] = base[2] - offZ;
        space[3] = base[0] + offX;
        space[4] = base[1] + yLen;
        space[5] = base[2] + offZ;
    };

    ICubeCreateStrategy NZ = (space, base, xLen, yLen, zLen) -> {
        float offX = xLen / 2, offY = yLen / 2;
        space[0] = base[0] - offX;
        space[1] = base[1] - offY;
        space[2] = base[2];
        space[3] = base[0] + offX;
        space[4] = base[1] + offY;
        space[5] = base[2] + zLen;
    };

    ICubeCreateStrategy PX = (space, base, xLen, yLen, zLen) -> {
        float offY = yLen / 2, offZ = zLen / 2;
        space[0] = base[0] - xLen;
        space[1] = base[1] - offY;
        space[2] = base[2] - offZ;
        space[3] = base[0];
        space[4] = base[1] + offY;
        space[5] = base[2] + offZ;
    };

    ICubeCreateStrategy PY = (space, base, xLen, yLen, zLen) -> {
        float offX = xLen / 2, offZ = zLen / 2;
        space[0] = base[0] - offX;
        space[1] = base[1] - yLen;
        space[2] = base[2] - offZ;
        space[3] = base[0] + offX;
        space[4] = base[1];
        space[5] = base[2] + offZ;
    };

    ICubeCreateStrategy PZ = (space, base, xLen, yLen, zLen) -> {
        float offX = xLen / 2, offY = yLen / 2;
        space[0] = base[0] - offX;
        space[1] = base[1] - offY;
        space[2] = base[2] - zLen;
        space[3] = base[0] + offX;
        space[4] = base[1] + offY;
        space[5] = base[2];
    };

    ICubeCreateStrategy N_CORNER = (space, base, xLen, yLen, zLen) -> {
        space[0] = base[0];
        space[1] = base[1];
        space[2] = base[2];
        space[3] = base[0] + xLen;
        space[4] = base[1] + yLen;
        space[5] = base[2] + zLen;
    };

    ICubeCreateStrategy P_CORNER = (space, base, xLen, yLen, zLen) -> {
        space[0] = base[0] - xLen;
        space[1] = base[1] - yLen;
        space[2] = base[2] - zLen;
        space[3] = base[0];
        space[4] = base[1];
        space[5] = base[2];
    };
}
