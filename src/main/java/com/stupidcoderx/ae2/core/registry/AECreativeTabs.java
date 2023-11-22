package com.stupidcoderx.ae2.core.registry;

import com.stupidcoderx.common.core.IRegistry;
import com.stupidcoderx.common.element.ModCreativeTab;

public class AECreativeTabs implements IRegistry {
    public static final ModCreativeTab MAIN = new ModCreativeTab(() -> AEItems.SILICON.stack(1), "ae2");

    public static void buildElements() {
    }
}
