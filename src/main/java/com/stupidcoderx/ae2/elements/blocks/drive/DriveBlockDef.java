package com.stupidcoderx.ae2.elements.blocks.drive;

import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import com.stupidcoderx.modding.element.block.BlockDef;
import com.stupidcoderx.modding.element.block.OrientableBlock;

public class DriveBlockDef extends BlockDef<OrientableBlock> {
    public DriveBlockDef(String id, String name) {
        super(id, name, new DriveBlock(getPeekProp()));
    }

    @Override
    protected ModelBuilder provideBlockModel() {
        return null;
    }
}

