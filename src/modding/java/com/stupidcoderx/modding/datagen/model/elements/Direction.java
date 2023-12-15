package com.stupidcoderx.modding.datagen.model.elements;

/**
 * 模型坐标系的规定方向
 */
public enum Direction {
    PX("east",true, 0, 3),
    PY("up",true, 1,4),
    PZ("south",true, 2,5),
    NX("west",false, 0,0),
    NY("down",false, 1,1),
    NZ("north",false, 2,2)
    ;
    public final boolean isPositive;
    public final int dim;
    public final int index;
    public final String id;

    Direction(String id, boolean isPositive, int dimIndex, int indexInArray) {
        this.id = id;
        this.isPositive = isPositive;
        this.dim = dimIndex;
        this.index = indexInArray;
    }

    static Direction get(boolean isPositive, int dimension) {
        return isPositive ? values()[dimension] : values()[dimension + 3];
    }

    boolean larger(float left, float right) {
        return isPositive ? left > right : left < right;
    }

    Direction opposite() {
        return isPositive ? values()[dim + 3] : values()[dim];
    }


    public static Direction[] x() {
        return new Direction[]{NX, PX};
    }

    public static Direction[] y() {
        return new Direction[]{NY, PY};
    }

    public static Direction[] z() {
        return new Direction[]{NZ, PZ};
    }

    public static Direction[] yz() {
        return new Direction[]{NY, PY, PZ, NZ};
    }

    public static Direction[] xy() {
        return new Direction[]{NY, PY, PX, NX};
    }

    public static Direction[] xz() {
        return new Direction[]{PX, NX, PZ, NZ};
    }
}
