package com.stupidcoderx.modding.orientation;

import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.util.orientation.BlockOrientation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.List;

class FullOrientationStrategy implements IOrientationStrategy{
    private final List<Property<?>> properties;

    FullOrientationStrategy() {
        this.properties = List.of(BlockStateProperties.FACING, SPIN);
    }

    @Override
    public Direction getFacing(BlockState state) {
        return state.getValue(BlockStateProperties.FACING);
    }

    @Override
    public int getSpin(BlockState state) {
        return state.getValue(SPIN);
    }

    @Override
    public BlockState setFacing(BlockState state, Direction facing) {
        return state.setValue(BlockStateProperties.FACING, facing);
    }

    @Override
    public BlockState setSpin(BlockState state, int spin) {
        return state.setValue(SPIN, spin);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
        Direction top = Direction.UP;
        Direction facing = context.getHorizontalDirection().getOpposite();
        var player = context.getPlayer();
        if (player != null) {
            if (player.getXRot() > 65) {
                top = facing.getOpposite();
                facing = Direction.UP;
            } else if (player.getXRot() < -65) {
                top = facing.getOpposite();
                facing = Direction.DOWN;
            }
        }
        state = setFacing(state, facing);
        return setSpin(state, BlockOrientation.get(facing, top).spin);
    }

    @Override
    public Collection<Property<?>> properties() {
        return properties;
    }

    @Override
    public void provideBlockState(ResourceLocation loc) {
        DataProviders.BLOCK_STATE.variants(loc)
                .variantFacing()
                .variant("spin", List.of("0", "1", "2", "3"))
                .condition(state -> {
                    Direction d = Direction.values()[state.optionIndex(0)];
                    int spin = state.optionIndex(1);
                    BlockOrientation o = BlockOrientation.get(d, spin);
                    return new Model(loc)
                            .rotationX(o.angleX / 90)
                            .rotationY(o.angleY / 90)
                            .rotationZ(o.angleZ / 90);
                });
    }
}
