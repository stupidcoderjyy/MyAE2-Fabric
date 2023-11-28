package com.stupidcoderx.ae2.core.registry;

import com.stupidcoderx.modding.element.ModCreativeTab;

public class AECreativeTabs{
    public static final ModCreativeTab MAIN
            = new ModCreativeTab(() -> AEBlocks.QUARTZ_BLOCK.stack(1), "ae2");

    public static void build() {
    }
}
