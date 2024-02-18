package com.stupidcoderx.modding.element.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ModEntityBlock<E extends BlockEntity> extends OrientableBlock implements EntityBlock {
    private BlockEntityType<E> beType;

    public ModEntityBlock(Properties properties) {
        super(properties);
    }

    public void setType(BlockEntityType<E> beType) {
        this.beType = beType;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return beType.create(blockPos, blockState);
    }
}
