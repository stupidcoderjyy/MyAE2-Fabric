package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class FenceGateBlockDef extends BlockDef<FenceGateBlock>{
    private Supplier<ResourceLocation> texture;

    public FenceGateBlockDef(String id, String name, WoodType type, BlockDef<?> baseBlock) {
        super(id, name, new FenceGateBlock(getPeekProp().noOcclusion(), type));
        if (Mod.isEnvDataGen) {
            this.texture = () -> getTexture(baseBlock, "all");
        }
    }

    @Override
    @DataGenOnly
    public void provideData() {
        super.provideData();
        DataProviders.BLOCK_TAGS.of(BlockTags.FENCE_GATES).add(block);
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc)
                .variant("facing", List.of("south", "west", "north", "east"))
                .variantBool("in_wall")
                .variantBool("open")
                .condition(state -> {
                    int y = state.optionIndex(0);   //facing
                    String suffix = "";
                    if (state.optionIndex(1) == 1) {
                        suffix += "_wall";
                    }
                    if (state.optionIndex(2) == 1) {
                        suffix += "_open";
                    }
                    return new Model(loc.withSuffix(suffix)).uvLock(true).rotationY(y);
                });
    }

    @Override
    @DataGenOnly
    protected @Nullable ModelBuilder provideBlockModel() {
        for(int i = 0 ; i < 4 ; i ++) {
            String suffix = "";
            if ((i & 1) == 1) { // 01
                suffix += "_wall";
            }
            if ((i & 2) == 2) { // 10
                suffix += "_open";
            }
            DataProviders.MODEL_BLOCK.model(loc.withSuffix(suffix))
                    .parent("block/template_fence_gate" + suffix)
                    .texture("texture", texture.get());
        }
        return null;
    }
}
