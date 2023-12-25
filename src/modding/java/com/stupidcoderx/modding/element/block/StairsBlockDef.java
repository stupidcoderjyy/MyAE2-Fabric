package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StairBlock;

import java.util.List;

public class StairsBlockDef extends ExtendedBlockDef<StairBlock> {
    StairsBlockDef(String id, String name, BlockDef<?> baseBlock) {
        super(id, name, baseBlock, new StairBlock(baseBlock.block.defaultBlockState(), getPeekProp()));
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc)
                .variant("facing", List.of("east", "south", "west", "north"))
                .variant("half", List.of("top", "bottom"))
                .variant("shape", List.of("straight", "inner_left", "inner_right", "outer_left", "outer_right"))
                .condition(state -> {
                    int yRot = state.optionIndex(0); //EAST为0°、SOUTH为90°、...
                    int xRot = 0;
                    boolean top = state.optionIndex(1) == 0;
                    if (top) { //top，沿+Z方向旋转180°
                        xRot = 2;
                    }
                    int shape = state.optionIndex(2);
                    if (shape == 1 || shape == 3) { // ..._left则需要顺时针转90°
                        yRot += 3;
                    }
                    if (shape != 0 && top) {
                        yRot ++;
                    }
                    ResourceLocation loc = this.loc;
                    if (shape != 0) {
                        loc = shape < 3 ? loc.withSuffix("_inner") : loc.withSuffix("_outer");
                    }
                    return new Model(loc).rotationX(xRot).rotationY(yRot).uvLock(xRot != 0 || yRot != 0);
                });
    }

    @Override
    @DataGenOnly
    protected ModelBuilder provideBlockModel() {
        ResourceLocation top = getBaseBlockTexture("top", "top", "end", "all");
        ResourceLocation side = getBaseBlockTexture("side", "side", "all");
        ResourceLocation bottom = getBaseBlockTexture("bottom", "bottom", "end", "all");
        DataProviders.MODEL_BLOCK.getOrCreateModel(loc.withSuffix("_inner"))
                .parent("block/inner_stairs")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
        DataProviders.MODEL_BLOCK.getOrCreateModel(loc.withSuffix("_outer"))
                .parent("block/outer_stairs")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
        return DataProviders.MODEL_BLOCK.getOrCreateModel(loc)
                .parent("block/stairs")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
    }
}
