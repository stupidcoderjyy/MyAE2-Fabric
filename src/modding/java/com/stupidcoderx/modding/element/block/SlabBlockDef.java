package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SlabBlock;

import java.util.List;
import java.util.function.Supplier;

public class SlabBlockDef extends BlockDef<SlabBlock> {
    @DataGenOnly
    private Supplier<ResourceLocation> modelFull, textureTop, textureSide, textureBottom;

    public SlabBlockDef(String id, String name, BlockDef<?> baseBlock) {
        super(id, name, new SlabBlock(getPeekProp()));
        if (Mod.isEnvDataGen) {
            this.modelFull = () -> baseBlock.loc;
            this.textureTop = () -> getTexture(baseBlock, "top", "end", "all");
            this.textureSide = () -> getTexture(baseBlock, "side", "all");
            this.textureBottom = () -> getTexture(baseBlock, "bottom", "end", "all");
        }
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc)
                .variant("type", List.of("bottom", "double", "top"))
                .condition(state -> switch (state.optionIndex(0)) {
                    case 0 -> new Model(loc);
                    case 1 -> new Model(modelFull.get());
                    default -> new Model(loc.withSuffix("_top"));
                });
    }

    @Override
    @DataGenOnly
    protected ModelBuilder provideBlockModel() {
        ResourceLocation top = textureTop.get();
        ResourceLocation side = textureSide.get();
        ResourceLocation bottom = textureBottom.get();
        DataProviders.MODEL_BLOCK.model(loc.withSuffix("_top"))
                .parent("block/slab_top")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
        return DataProviders.MODEL_BLOCK.model(loc)
                .parent("block/slab")
                .texture("top", top).texture("side", side).texture("bottom", bottom);
    }
}
