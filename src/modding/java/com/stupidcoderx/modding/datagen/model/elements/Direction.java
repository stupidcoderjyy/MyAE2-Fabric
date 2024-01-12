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

    private static final Direction[] CLOCKWISE_X = new Direction[]{PY, NZ, NY, PZ};
    private static final Direction[] CLOCKWISE_Y = new Direction[]{PX, PZ, NX, NZ};
    private static final Direction[] CLOCKWISE_Z = new Direction[]{PX, PY, NX, PZ};

    public Direction clockwise(ActionRecord ar) {
        return clockwise(ar.rotDim(), ar.rotCount());
    }

    public Direction clockwise(int dim, int count) {
        if (this.dim == dim) {
            return this;
        }
        Direction result = null;
        switch (dim) {
            case 0 -> {
                switch (this) {
                    case PZ -> result = CLOCKWISE_X[(3 + count) % 4];
                    case NY -> result = CLOCKWISE_X[(2 + count) % 4];
                    case NZ -> result = CLOCKWISE_X[(1 + count) % 4];
                    case PY -> result = CLOCKWISE_X[count % 4];
                }
            }
            case 1 -> {
                switch (this) {
                    case NZ -> result = CLOCKWISE_Y[(3 + count) % 4];
                    case NX -> result = CLOCKWISE_Y[(2 + count) % 4];
                    case PZ -> result = CLOCKWISE_Y[(1 + count) % 4];
                    case PX -> result = CLOCKWISE_Y[count % 4];
                }
            }
            case 2 -> {
                switch (this) {
                    case PZ -> result = CLOCKWISE_Z[(3 + count) % 4];
                    case NX -> result = CLOCKWISE_Z[(2 + count) % 4];
                    case PY -> result = CLOCKWISE_Z[(1 + count) % 4];
                    case PX -> result = CLOCKWISE_Z[count % 4];
                }
            }
        }
        if (result == null) { //不可能出现这种情况
            throw new RuntimeException("");
        }
        return result;
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
