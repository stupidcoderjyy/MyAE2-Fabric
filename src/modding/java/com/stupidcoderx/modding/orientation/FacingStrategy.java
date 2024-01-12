package com.stupidcoderx.modding.orientation;

import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.util.orientation.BlockOrientation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

class FacingStrategy implements IOrientationStrategy{
    private final DirectionProperty directionProp;
    private final List<Property<?>> properties;
    private final boolean allowsPlayerRotation;

    FacingStrategy(DirectionProperty property, boolean allowsPlayerRotation) {
        this.directionProp = property;
        this.properties = Collections.singletonList(property);
        this.allowsPlayerRotation = allowsPlayerRotation;
    }

    @Override
    public Collection<Property<?>> properties() {
        return properties;
    }

    @Override
    public void provideBlockState(ResourceLocation loc) {
        DataProviders.BLOCK_STATE.variants(loc)
                .variantFacing()
                .condition(state -> {
                    Direction facing = Direction.values()[state.optionIndex(0)];
                    BlockOrientation o = BlockOrientation.get(facing, 0);
                    return new Model(loc)
                            .rotationX(o.angleX / 90)
                            .rotationY(o.angleY / 90);
                });
    }

    @Override
    public BlockState setFacing(BlockState state, Direction facing) {
        if (!directionProp.getPossibleValues().contains(facing)) {
            return state;
        }
        return state.setValue(directionProp, facing);
    }

    @Override
    public Direction getFacing(BlockState state) {
        return state.getValue(directionProp);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
        return setFacing(state, context.getClickedFace());
    }

    @Override
    public boolean allowPlayerRotation() {
        return allowsPlayerRotation;
    }
}
