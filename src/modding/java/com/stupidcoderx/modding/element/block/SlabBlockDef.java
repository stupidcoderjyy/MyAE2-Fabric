package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SlabBlock;

import java.util.List;

public class SlabBlockDef extends ExtendedBlockDef<SlabBlock> {
    private ResourceLocation locTop;

    SlabBlockDef(String id, String name, BlockDef<?> baseBlock) {
        super(id, name, baseBlock, new SlabBlock(getPeekProp()));
        if (Mod.isEnvDataGen) {
            locTop = loc.withSuffix("_top");
        }
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc)
                .variant("type", List.of("bottom", "double", "top"))
                .condition(state -> switch (state.optionIndex(0)) {
                    case 0 -> new Model(loc);
                    case 1 -> new Model(baseBlock.loc);
                    default -> new Model(locTop);
                });
    }

    @Override
    @DataGenOnly
    protected ModelBuilder provideBlockModel() {
        ResourceLocation top = getBaseBlockTexture("top", "top", "end", "all");
        ResourceLocation side = getBaseBlockTexture("side", "side", "all");
        ResourceLocation bottom = getBaseBlockTexture("bottom", "bottom", "end", "all");
        DataProviders.MODEL_BLOCK.getOrCreateModel(locTop)
                .parent("block/slab_top")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
        return DataProviders.MODEL_BLOCK.getOrCreateModel(loc)
                .parent("block/slab")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
    }
}
