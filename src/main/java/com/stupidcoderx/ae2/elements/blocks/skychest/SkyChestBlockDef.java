package com.stupidcoderx.ae2.elements.blocks.skychest;

import com.stupidcoderx.ae2.registry.AEBlocks;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import com.stupidcoderx.modding.element.block.EntityBlockDef;
import com.stupidcoderx.modding.element.block.ModEntityBlock;
import org.jetbrains.annotations.Nullable;

public class SkyChestBlockDef extends EntityBlockDef<ModEntityBlock<?>> {
    public SkyChestBlockDef(String id, String name) {
        super(id, name, new SkyChestBlock(getPeekProp().noOcclusion()));
    }

    @Override
    protected void provideItemModel() {
    }

    @Override
    protected @Nullable ModelBuilder provideBlockModel() {
        return DataProviders.MODEL_BLOCK.model(loc).texture("particle", AEBlocks.SKY_STONE_BLOCK.loc);
    }

    @Override
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc).condition(state -> new Model(loc));
    }
}
