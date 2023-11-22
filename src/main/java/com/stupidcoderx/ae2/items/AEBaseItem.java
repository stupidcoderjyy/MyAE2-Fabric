package com.stupidcoderx.ae2.items;

import com.stupidcoderx.common.element.BaseItem;
import net.minecraft.resources.ResourceLocation;

public class AEBaseItem extends BaseItem<AEBaseItem> {
    public AEBaseItem(Properties properties, ResourceLocation location) {
        super(properties, location);
    }

    public AEBaseItem(ResourceLocation location) {
        super(location);
    }

    public AEBaseItem(String id) {
        super(id);
    }
}
