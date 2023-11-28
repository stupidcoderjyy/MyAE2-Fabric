package com.stupidcoderx.ae2.core.registry;

import com.stupidcoderx.ae2.items.PaintBallItem;
import com.stupidcoderx.modding.element.BaseItem;

public class AEItems {
    public static final BaseItem SILICON = new BaseItem("silicon").creativeTab(AECreativeTabs.MAIN);

    static {
        PaintBallItem.create();
    }

    public static void build() {
    }
}
