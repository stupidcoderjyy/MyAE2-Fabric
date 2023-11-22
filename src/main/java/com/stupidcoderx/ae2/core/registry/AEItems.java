package com.stupidcoderx.ae2.core.registry;

import com.stupidcoderx.ae2.items.PaintBallItem;
import com.stupidcoderx.common.core.IRegistry;
import com.stupidcoderx.common.item.BaseItem;

public class AEItems implements IRegistry {
    public static final BaseItem SILICON = new BaseItem("silicon");

    static {
        PaintBallItem.create();
    }
}
