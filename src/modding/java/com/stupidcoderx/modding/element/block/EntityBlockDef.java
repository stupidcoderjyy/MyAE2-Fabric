package com.stupidcoderx.modding.element.block;

public class EntityBlockDef<B extends ModEntityBlock<?>> extends BlockDef<B> {
    public EntityBlockDef(String id, String name, B block) {
        super(id, name, block);
    }
}
