package com.stupidcoderx.ae2.items;

import com.stupidcoderx.ae2.util.AEColor;
import com.stupidcoderx.common.core.Mod;
import com.stupidcoderx.common.datagen.generators.ItemModelGenerator;
import com.stupidcoderx.common.item.BaseItem;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;

public class PaintBallItem extends BaseItem {
    private static final Map<AEColor, PaintBallItem> ballsNormal = new EnumMap<>(AEColor.class);
    private static final Map<AEColor, PaintBallItem> ballsLumen = new EnumMap<>(AEColor.class);
    public final boolean isLumen;
    public final AEColor color;

    public PaintBallItem(boolean isLumen, AEColor color) {
        super(getLoc(isLumen, color));
        this.isLumen = isLumen;
        this.color = color;
        if (isLumen) {
            ballsLumen.put(color, this);
        } else {
            ballsNormal.put(color, this);
        }
    }

    private static ResourceLocation getLoc(boolean isLumen, AEColor color) {
        String name = color.id;
        if (isLumen) {
            name += "_lumen";
        }
        name += "_paint_ball";
        return new ResourceLocation(Mod.getModId(), name);
    }

    public static void create() {
        for (AEColor c : AEColor.values()) {
            new PaintBallItem(false, c);
        }
        for (AEColor c : AEColor.values()) {
            new PaintBallItem(true, c);
        }
    }

    public static PaintBallItem fromColor(AEColor color, boolean isLumen) {
        if (isLumen) {
            return ballsLumen.get(color);
        }
        return ballsNormal.get(color);
    }

    @Override
    public void commonRegister() {
        ColorProviderRegistry.ITEM.register((stack, tint) -> calcColor(), this);
        super.commonRegister();
    }

    private int calcColor() {
        int rgb = color.rgbNormal;
        int r = rgb >> 16 & 0xff;
        int g = rgb >> 8 & 0xff;
        int b = rgb & 0xff;
        if (isLumen) {
            //略微提升亮度
            float fail = 0.7f;
            float full = 0xff * 0.3f;
            rgb = (int)(full + r * fail) << 16
                    | (int)(full + g * fail) << 8
                    | (int)(full + b * fail);
        }
        return rgb | (0xff << 24);
    }

    @Override
    public void buildData() {
        ItemModelGenerator.getInstance()
                .model(location)
                .parent("minecraft:item/generated")
                .texture("layer0", isLumen ? "paint_ball_shimmer" : "paint_ball");
    }
}
