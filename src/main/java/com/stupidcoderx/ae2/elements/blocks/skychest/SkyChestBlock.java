package com.stupidcoderx.ae2.elements.blocks.skychest;

import com.stupidcoderx.ae2.elements.blockentities.SkyChestTE;
import com.stupidcoderx.modding.element.block.ModEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkyChestBlock extends ModEntityBlock<SkyChestTE> {
    public static final VoxelShape SHAPE = Block.box(1,0,1,14,14,14);

    public SkyChestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
}
