package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.modding.element.ModCreativeTab;

public class AECreativeTabs{
    public static final ModCreativeTab MAIN = new ModCreativeTab(
            "main", "应用能源2", () -> AEBlocks.QUARTZ_BLOCK);

    public static void build() {
    }
}
