package com.stupidcoderx.modding.datagen.model.elements;

/**
 * 模型坐标系的规定方向
 */
public enum Direction {
    PX_EAST("east",true, 0, 3),
    PY_UP("up",true, 1,4),
    PZ_SOUTH("south",true, 2,5),
    NX_WEST("west",false, 0,0),
    NY_DOWN("down",false, 1,1),
    NZ_NORTH("north",false, 2,2)
    ;
    public final boolean isPositive;
    public final int dim;
    public final int index;
    public final String id;

    private static final Direction[] UD_NS = {PY_UP, NY_DOWN, NZ_NORTH, PZ_SOUTH};
    private static final Direction[] WE_NS = {PX_EAST, NX_WEST, NZ_NORTH, PZ_SOUTH};
    private static final Direction[] UD_WE = {PY_UP, NY_DOWN, PX_EAST, NX_WEST};

    Direction(String id, boolean isPositive, int dimIndex, int indexInArray) {
        this.id = id;
        this.isPositive = isPositive;
        this.dim = dimIndex;
        this.index = indexInArray;
    }

    static Direction get(boolean isPositive, int dimension) {
        return isPositive ? values()[dimension] : values()[dimension + 3];
    }

    public static Direction[] getVertical(Direction d) {
        return switch (d.dim) {
            case 0 -> UD_NS;
            case 1 -> WE_NS;
            case 2 -> UD_WE;
            default -> new Direction[0];
        };
    }

    boolean larger(float left, float right) {
        return isPositive ? left > right : left < right;
    }

    Direction opposite() {
        return isPositive ? values()[dim + 3] : values()[dim];
    }
}
