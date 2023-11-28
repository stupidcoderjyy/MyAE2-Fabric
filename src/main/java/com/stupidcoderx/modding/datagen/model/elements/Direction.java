package com.stupidcoderx.modding.datagen.model.elements;

/**
 * 模型坐标系的规定方向
 */
public enum Direction {
    SOUTH(true, 0),
    TOP(true, 1),
    WEST(true, 2),
    NORTH(false, 0),
    DOWN(false, 1),
    EAST(false, 2)
    ;
    public final boolean isPositive;
    public final int dim;

    private static final Direction[] TD_NS = {TOP, DOWN, NORTH, SOUTH};
    private static final Direction[] WE_NS = {EAST, WEST, NORTH, SOUTH};
    private static final Direction[] TD_WE = {TOP, DOWN, EAST, WEST};

    Direction(boolean isPositive, int dimIndex) {
        this.isPositive = isPositive;
        this.dim = dimIndex;
    }

    static Direction get(boolean isPositive, int dimension) {
        return isPositive ? values()[dimension] : values()[dimension + 3];
    }

    public static Direction[] getVertical(Direction d) {
        return switch (d.dim) {
            case 0 -> TD_NS;
            case 1 -> WE_NS;
            case 2 -> TD_WE;
            default -> new Direction[0];
        };
    }
}
