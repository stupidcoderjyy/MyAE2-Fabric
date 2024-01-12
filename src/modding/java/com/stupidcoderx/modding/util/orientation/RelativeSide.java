package com.stupidcoderx.modding.util.orientation;

import net.minecraft.core.Direction;

public enum RelativeSide {
    FRONT(Direction.NORTH),
    BACK(Direction.SOUTH),
    TOP(Direction.UP),
    BOTTOM(Direction.DOWN),
    LEFT(Direction.WEST),
    RIGHT(Direction.EAST)
    ;

    public final Direction side;

    RelativeSide(Direction side) {
        this.side = side;
    }
}
