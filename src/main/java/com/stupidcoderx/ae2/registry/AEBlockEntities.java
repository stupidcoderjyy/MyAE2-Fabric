package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.blockentities.SkyChestTE;
import com.stupidcoderx.modding.element.blockentity.TEDef;

public class AEBlockEntities {
    public static final TEDef<SkyChestTE> SKY_CHEST;

    static {
        SKY_CHEST = new TEDef<>("sky_chest", SkyChestTE::new, AEBlocks.SKY_STONE_CHEST);
    }

    public static void build() {
    }
}
