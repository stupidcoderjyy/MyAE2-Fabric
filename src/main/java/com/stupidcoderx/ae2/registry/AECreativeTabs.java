package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.modding.element.ModCreativeTab;

public class AECreativeTabs{
    public static final ModCreativeTab MAIN = new ModCreativeTab(() -> AEBlocks.QUARTZ_BLOCK, "main");
    public static final ModCreativeTab TOOLS = new ModCreativeTab(() -> AEItems.FLUIX_AXE, "tools");

    public static void build() {
    }
}