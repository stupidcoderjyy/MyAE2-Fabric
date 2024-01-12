package com.stupidcoderx.modding.util.orientation;

import com.stupidcoderx.modding.orientation.IOrientationStrategy;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * 模组中的机器方块往往需要能够更加自由地旋转（能够绕x、y、z三个轴旋转），以实现更自由的物品输入和输出。
 * 方块方位定义了所有可能的方块放置情况
 */
public enum BlockOrientation {
    DOWN_NORTH(90, 0, 0, 0),
    DOWN_WEST(90, 0, 270, 1),
    DOWN_SOUTH(90, 0, 180, 2),
    DOWN_EAST(90, 0, 90, 3),

    UP_NORTH(270, 0, 180, 0),
    UP_EAST(270, 0, 90, 1),
    UP_SOUTH(270, 0, 0, 2),
    UP_WEST(270, 0, 270, 3),

    NORTH_UP(0, 0, 0, 0),
    NORTH_WEST(0, 0, 270, 1),
    NORTH_DOWN(0, 0, 180, 2),
    NORTH_EAST(0, 0, 90, 3),

    SOUTH_UP(0, 180, 0, 0),
    SOUTH_EAST(0, 180, 90, 1),
    SOUTH_DOWN(0, 180, 180, 2),
    SOUTH_WEST(0, 180, 270, 3),

    WEST_UP(0, 270, 0, 0),
    WEST_SOUTH(0, 270, 270, 1),
    WEST_DOWN(0, 270, 180, 2),
    WEST_NORTH(0, 270, 90, 3),

    EAST_UP(0, 90, 0, 0),
    EAST_NORTH(0, 90, 270, 1),
    EAST_DOWN(0, 90, 180, 2),
    EAST_SOUTH(0, 90, 90, 3)
    ;
    public final int angleX, angleY, angleZ;
    public final int spin;
    public final Quaternionf rotator;

    /**
     * 建立了从任一方向到旋转后方向的映射，旋转由{@link #angleX}、{@link #angleY}和{@link #angleZ}决定。<p>
     * 也可以理解为：方块处于当前方向时，某个相对方向{@link RelativeSide}所对应的实际方向
     */
    private final Direction[] rotatedSideTo;

    /**
     * @param angleX 方块处于当前朝向时，模型绕x轴旋转的角度
     * @param angleY 方块处于当前朝向时，模型绕y轴旋转的角度
     * @param angleZ 方块处于当前朝向时，模型绕z轴旋转的角度
     * @param spin 方块围绕面旋转的次数（右手法则）
     */
    BlockOrientation(int angleX, int angleY, int angleZ, int spin) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;
        this.spin = spin;
        // 模型旋转的角度遵守左手法则（见备忘录-BlockState模型旋转），而四元数遵守右手法则
        this.rotator = new Quaternionf().rotationXYZ(
                -angleX * Mth.DEG_TO_RAD,
                -angleY * Mth.DEG_TO_RAD,
                -angleZ * Mth.DEG_TO_RAD);
        this.rotatedSideTo = new Direction[6];
        for (Direction d : Direction.values()) {
            Vector3f normal = d.step().rotate(rotator);
            Direction res = Direction.getNearest(normal.x, normal.y, normal.z);
            rotatedSideTo[d.ordinal()] = res;
        }
    }

    /**
     * @param facing 方块的朝向
     * @param top 方块顶部朝向
     * @return 方块方位
     */
    public static BlockOrientation get(Direction facing, Direction top) {
        int off = facing.ordinal() << 2;
        for (int i = 0 ; i < 4 ; i++) {
            BlockOrientation o = values()[off + i];
            if (o.side(RelativeSide.TOP) == top) {
                return o;
            }
        }
        return values()[off];
    }

    public static BlockOrientation get(Direction facing, int spin) {
        return values()[facing.ordinal() << 2 | spin];
    }

    public static BlockOrientation get(IOrientationStrategy strategy, BlockState state) {
        Direction facing = strategy.getFacing(state);
        int spin = strategy.getSpin(state);
        return get(facing, spin);
    }

    /**
     * 获得某个相对方向{@link RelativeSide}对应面的实际方向
     * @param side 相对方向
     * @return 对应面所指向的实际方向
     */
    public Direction side(RelativeSide side) {
        return rotatedSideTo[side.side.ordinal()];
    }

    /**
     * 获得围绕某个面顺时针（左手法则）旋转90°后的方块方位
     * @param face 围绕的面
     * @return 旋转后的方块方位
     */
    public BlockOrientation clockwise(Direction face) {
        Direction facing = side(RelativeSide.FRONT);
        Direction top = side(RelativeSide.TOP); //获得方块朝向和顶部的实际方向
        Direction newFacing, newTop;
        Direction.Axis axis = face.getAxis();
        if (face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            newFacing = facing.getClockWise(axis);
            newTop = top.getClockWise(axis);
        } else {
            newFacing = facing.getCounterClockWise(axis);
            newTop = top.getCounterClockWise(axis);
        }
        return get(newFacing, newTop);
    }
}
