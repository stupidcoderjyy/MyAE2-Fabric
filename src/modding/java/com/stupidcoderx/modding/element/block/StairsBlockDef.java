package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StairBlock;

import java.util.List;
import java.util.function.Supplier;

public class StairsBlockDef extends BlockDef<StairBlock> {
    @DataGenOnly
    private Supplier<ResourceLocation> textureTop, textureSide, textureBottom;

    public StairsBlockDef(String id, String name, BlockDef<?> baseBlock) {
        super(id, name, new StairBlock(baseBlock.block.defaultBlockState(), getPeekProp()));
        if (Mod.isEnvDataGen) {
            this.textureTop = () -> getTexture(baseBlock, "top", "end", "all");
            this.textureSide = () -> getTexture(baseBlock, "side", "all");
            this.textureBottom = () -> getTexture(baseBlock, "bottom", "end", "all");
        }
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
        ResourceLocation top = textureTop.get();
        ResourceLocation side = textureSide.get();
        ResourceLocation bottom = textureBottom.get();
        DataProviders.MODEL_BLOCK.model(loc.withSuffix("_inner"))
                .parent("block/inner_stairs")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
        DataProviders.MODEL_BLOCK.model(loc.withSuffix("_outer"))
                .parent("block/outer_stairs")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
        return DataProviders.MODEL_BLOCK.model(loc)
                .parent("block/stairs")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
    }
}
