package com.stupidcoderx.ae2.elements.blocks;

import com.stupidcoderx.ae2.registry.AEBlocks;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import com.stupidcoderx.modding.element.block.BaseEntityBlock;
import com.stupidcoderx.modding.element.block.EntityBlockDef;
import org.jetbrains.annotations.Nullable;

public class DisplayBlockDef extends EntityBlockDef<BaseEntityBlock<?>> {
    public DisplayBlockDef(String id, String name) {
        super(id, name, new BaseEntityBlock<>(getPeekProp()));
    }

    @Override
    protected void provideItemModel() {
    }

    @Override
    protected @Nullable ModelBuilder provideBlockModel() {
        return null;
    }

    @Override
    protected void provideBlockState() {
        //仅用于粒子效果
        DataProviders.BLOCK_STATE.variants(loc).condition(state -> new Model(AEBlocks.SKY_STONE.loc));
    }
}
