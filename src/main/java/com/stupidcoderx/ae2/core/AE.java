package com.stupidcoderx.ae2.core;

import com.stupidcoderx.ae2.core.registry.AEItems;
import com.stupidcoderx.common.core.Mod;

public abstract class AE extends Mod {
    public static final String MOD_ID = "ae2";

    public AE() {
        super(MOD_ID);
        registries.add(new AEItems());
        commonInit();
        finishInit();
    }
}

