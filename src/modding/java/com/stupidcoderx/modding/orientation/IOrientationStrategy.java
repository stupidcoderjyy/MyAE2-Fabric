package com.stupidcoderx.modding.orientation;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.util.orientation.BlockOrientation;
import com.stupidcoderx.modding.util.orientation.RelativeSide;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;

/**
 * 方块的方位策略，限制了方块能够处于的方位种类
 */
public interface IOrientationStrategy {
    IntegerProperty SPIN = IntegerProperty.create("spin", 0, 3);

    static IOrientationStrategy get(BlockState state) {
        if (state.getBlock() instanceof IOrientableBlock orientableBlock) {
            return orientableBlock.orientationStrategy();
        }
        return OrientationStrategies.NONE;
    }

    /**
     * 策略所允许的所有方位对应的方块状态
     * @return 所有方块状态组成的集合
     */
    Collection<Property<?>> properties();

    @DataGenOnly
    void provideBlockState(ResourceLocation loc);

    /**
     * 获取在当前方位策略下，一个方块所应当具有的方块状态，
     * @param state 未根据策略进行更改的方块状态
     * @param context 方块放置的环境
     * @return 更改后的方块状态
     */
    default BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
        return state;
    }

    default BlockState setFacing(BlockState state, Direction facing) {
        return state;
    }

    default BlockState setSpin(BlockState state, int spin) {
        return state;
    }

    default int getSpin(BlockState state) {
        return 0;
    }

    default Direction getFacing(BlockState state) {
        return Direction.NORTH;
    }

    default BlockState setOrientation(BlockState state, Direction facing, int spin) {
        return setSpin(setFacing(state, facing), spin);
    }

    default BlockState setOrientation(BlockState state, BlockOrientation orientation) {
        return setOrientation(state, orientation.side(RelativeSide.FRONT), orientation.spin);
    }

    /**
     * @return 是否允许玩家对方块进行主动旋转（如扳手）
     */
    default boolean allowPlayerRotation() {
        return true;
    }
}
