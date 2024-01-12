package com.stupidcoderx.ae2.elements.blocks.drive;

import com.stupidcoderx.modding.element.block.OrientableBlock;
import com.stupidcoderx.modding.util.orientation.BlockOrientation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DriveBlock extends OrientableBlock {
    public DriveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        Direction clickedFace = blockHitResult.getDirection();
        BlockOrientation orientation = BlockOrientation
                .get(orientationStrategy(), state)
                .clockwise(clickedFace);
        BlockState newState = orientationStrategy().setOrientation(state, orientation);
        if (newState != state) {
            level.setBlockAndUpdate(pos, newState);
        }
        return InteractionResult.sidedSuccess(true);
    }
}
