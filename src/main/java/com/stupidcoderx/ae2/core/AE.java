package com.stupidcoderx.ae2.core;

import com.stupidcoderx.ae2.registry.AEBlocks;
import com.stupidcoderx.ae2.registry.AECreativeTabs;
import com.stupidcoderx.ae2.registry.items.AEItems;
import com.stupidcoderx.modding.core.Mod;

public class AE extends Mod {
    public static final String MOD_ID = "ae2";

    protected AE() {
        super(MOD_ID);
    }

    @Override
    protected void buildElements() {
        AECreativeTabs.build();
        AEItems.build();
        AEBlocks.build();
    }
}

