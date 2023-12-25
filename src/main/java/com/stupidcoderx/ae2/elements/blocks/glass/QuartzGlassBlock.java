package com.stupidcoderx.ae2.elements.blocks.glass;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;

public class QuartzGlassBlock extends AbstractGlassBlock {
    public QuartzGlassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction direction) {
        return adjacentBlockState.getBlock() instanceof QuartzGlassBlock;
    }
}
