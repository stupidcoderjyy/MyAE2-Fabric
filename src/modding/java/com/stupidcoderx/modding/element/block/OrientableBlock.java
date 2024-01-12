package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.orientation.IOrientableBlock;
import com.stupidcoderx.modding.orientation.IOrientationStrategy;
import com.stupidcoderx.modding.orientation.OrientationStrategies;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class OrientableBlock extends Block implements IOrientableBlock {
    public OrientableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public IOrientationStrategy orientationStrategy() {
        return OrientationStrategies.FULL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        for (Property<?> p : orientationStrategy().properties()) {
            builder.add(p);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return orientationStrategy().getStateForPlacement(this.defaultBlockState(), ctx);
    }
}
