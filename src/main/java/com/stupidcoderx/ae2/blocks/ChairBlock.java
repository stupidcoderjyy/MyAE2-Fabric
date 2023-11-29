package com.stupidcoderx.ae2.blocks;

import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import com.stupidcoderx.modding.datagen.model.elements.Direction;
import com.stupidcoderx.modding.datagen.model.elements.SeparationConfig;
import com.stupidcoderx.modding.datagen.model.elements.Structure;
import com.stupidcoderx.modding.element.BaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChairBlock extends BaseBlock {
    private static final VoxelShape CHAIR_SHAPE;
    private static final Structure structure = new Structure();

    public ChairBlock(Properties properties, String id) {
        super(properties, id);
    }

    @Override
    protected void generateModel() {
        ModelBuilder mbChair = DataProviders.MODEL_BLOCK.getOrCreateModel(registryLoc)
                .parent("minecraft:block/cube_all")
                .texture("0", "minecraft:block/oak_log")
                .texture("1", "minecraft:block/smooth_stone")
                .texture("particle", "minecraft:block/oak_log")
                .struct(structure);
        generateItemModel(mbChair);
    }

    @Override
    public void close() {
        structure.clear();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return CHAIR_SHAPE;
    }

    static {
        CHAIR_SHAPE = structure.create(16, 16, 16)
                .process(c -> c.faceAll().uv(0, 0).texture("#0"))
                .scoop(config -> config
                        .dimensionSeq(SeparationConfig.YZX)
                        .range(12, 14, 16)
                        .align(Direction.DOWN, structure.getOutline(Direction.DOWN)))
                .scoop(config -> config
                        .range(16, 14, 12)
                        .align(Direction.DOWN, structure.getOutline(Direction.DOWN)))
                .findMost(Direction.UP)
                .process(c -> c.face(Direction.UP).texture("#1"))
                .findMost(Direction.DOWN)
                .process(c -> c.face(Direction.UP).removeFaces())
                .globalAlign(Direction.DOWN, Direction.NORTH, Direction.WEST)
                .toVoxelShape();
    }
}