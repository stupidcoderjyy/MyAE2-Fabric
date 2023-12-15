package com.stupidcoderx.ae2.elements.blocks;

import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import com.stupidcoderx.modding.datagen.model.elements.*;
import com.stupidcoderx.modding.element.BaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChairBlock extends BaseBlock {
    private static final VoxelShape CHAIR_SHAPE;
    private static final Structure structure = new Structure(IBasePointStrategy.NY)
            .cubeCreateStrategy(ICubeCreateStrategy.NY);

    static {
        CHAIR_SHAPE = structure.create(16, 16, 16)
                .process(c -> c.faceAll().uv(0, 0).texture("#0"))
                .scoop(config -> config
                        .dimensionSeq(SeparationConfig.YZX)
                        .range(12, 14, 16)
                        .separateAction(Direction.PY, c -> c.face(Direction.PY).texture("#1"))
                        .separateAction(d -> d != Direction.PY, c -> c.face(Direction.PY).removeFaces()))
                .scoop(config -> config.range(16, 14, 12))
                .toVoxelShape();
    }

    public ChairBlock(Properties properties, String id) {
        super(properties, id);
    }

    @Override
    protected void generateBlockModel(ModelBuilder mbBlock) {
        mbBlock.parent("minecraft:block/cube_all")
                .texture("0", "minecraft:block/oak_log")
                .texture("1", "minecraft:block/smooth_stone")
                .texture("particle", "minecraft:block/oak_log")
                .struct(structure);
    }

    @Override
    public void close() {
        structure.clear();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return CHAIR_SHAPE;
    }
}