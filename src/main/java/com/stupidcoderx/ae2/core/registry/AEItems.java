package com.stupidcoderx.ae2.core.registry;

import com.stupidcoderx.ae2.items.AEBaseItem;
import com.stupidcoderx.ae2.items.PaintBallItem;
import com.stupidcoderx.common.core.IRegistry;

public class AEItems implements IRegistry {
    public static final AEBaseItem SILICON = new AEBaseItem("silicon")
            .setCreativeTab(AECreativeTabs.MAIN);

    static {
        PaintBallItem.create();
    }

    public static void buildElements() {
    }
}
