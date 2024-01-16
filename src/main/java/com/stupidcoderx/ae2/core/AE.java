package com.stupidcoderx.ae2.core;

import com.stupidcoderx.ae2.registry.AEBlocks;
import com.stupidcoderx.ae2.registry.AECreativeTabs;
import com.stupidcoderx.ae2.registry.AEItems;
import com.stupidcoderx.ae2.registry.AERecipes;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.element.item.ItemDef;

public class AE extends Mod {
    public static final String MOD_ID = "ae2";

    protected AE() {
        super(MOD_ID);
    }

    @Override
    protected void buildElements() {
        ItemDef.pushTab(AECreativeTabs.MAIN);
        AEItems.build();
        AEBlocks.build();
        AERecipes.build();
    }
}

