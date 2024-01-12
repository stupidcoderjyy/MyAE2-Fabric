package com.stupidcoderx.modding.orientation;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class OrientationStrategies {
    /**
     * 方块的方位无法被更改
     */
    public static final IOrientationStrategy NONE = new NoOrientationStrategy();

    /**
     * 方块能够朝向水平的四个方向
     */
    public static final IOrientationStrategy HORIZONTAL_FACING = new HorizontalFacingStrategy();

    /**
     * 方块能够朝向六个方向
     */
    public static final IOrientationStrategy FACING = new FacingStrategy(
            BlockStateProperties.FACING, true);

    /**
     * 方块能够朝向六个方向，但方向无法被手动改变
     */
    public static final IOrientationStrategy FACING_NO_PLAYER_ROTATION = new FacingStrategy(
            BlockStateProperties.FACING, false);

    /**
     * 方块能够朝向六个方向，且能够围绕面的法线进行旋转
     */
    public static final IOrientationStrategy FULL = new FullOrientationStrategy();
}
