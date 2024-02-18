package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.blockentities.SkyChestTE;
import com.stupidcoderx.modding.element.blockentity.TEDef;

public class AEBlockEntities {
    public static final TEDef<SkyChestTE> DISPLAY;

    static {
        DISPLAY = new TEDef<>("display", SkyChestTE::new, AEBlocks.SKY_STONE_CHEST);
    }

    public static void build() {
    }
}
