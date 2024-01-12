package com.stupidcoderx.modding.orientation;

import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.util.orientation.BlockOrientation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

class HorizontalFacingStrategy extends FacingStrategy{
    HorizontalFacingStrategy() {
        super(BlockStateProperties.HORIZONTAL_FACING, true);
    }

    @Override
    public void provideBlockState(ResourceLocation loc) {
        DataProviders.BLOCK_STATE.variants(loc)
                .variantFacing()
                .condition(state -> switch (state.optionIndex(0)) {
                    case 0, 1 -> new Model(loc);    // up down
                    default -> {
                        Direction facing = Direction.values()[state.optionIndex(0)];
                        BlockOrientation o = BlockOrientation.get(facing, 0);
                        yield new Model(loc)
                                .rotationX(o.angleX / 90)
                                .rotationY(o.angleY / 90);
                    }
                });
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
        return setFacing(state, context.getHorizontalDirection().getOpposite());
    }
}
