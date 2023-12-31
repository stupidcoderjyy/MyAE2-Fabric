package com.stupidcoderx.ae2.core;

import com.stupidcoderx.ae2.registry.AEBlocks;
import com.stupidcoderx.ae2.registry.AEItems;
import com.stupidcoderx.ae2.registry.AERecipes;
import com.stupidcoderx.modding.core.Mod;

public class AE extends Mod {
    public static final String MOD_ID = "ae2";

    protected AE() {
        super(MOD_ID);
    }

    @Override
    protected void buildElements() {
        AEItems.build();
        AEBlocks.build();
        AERecipes.build();
    }
}

