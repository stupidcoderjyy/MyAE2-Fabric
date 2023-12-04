package com.stupidcoderx.ae2.elements.items;

import com.stupidcoderx.ae2.registry.AECreativeTabs;
import com.stupidcoderx.ae2.util.AEColor;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.element.item.ItemDef;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;

public class PaintBallItemDef extends ItemDef<Item> {
    private static final Map<AEColor, PaintBallItemDef> ballsNormal = new EnumMap<>(AEColor.class);
    private static final Map<AEColor, PaintBallItemDef> ballsLumen = new EnumMap<>(AEColor.class);
    public final boolean isLumen;
    public final AEColor color;

    private PaintBallItemDef(boolean isLumen, AEColor color) {
        super(getLoc(isLumen, color), new Item(new Item.Properties()));
        this.isLumen = isLumen;
        this.color = color;
        if (isLumen) {
            ballsLumen.put(color, this);
        } else {
            ballsNormal.put(color, this);
        }
        setCreativeTab(AECreativeTabs.MAIN);
    }

    public static void create() {
        for (AEColor c : AEColor.values()) {
            new PaintBallItemDef(false, c);
        }
        for (AEColor c : AEColor.values()) {
            new PaintBallItemDef(true, c);
        }
    }

    public static PaintBallItemDef get(AEColor color, boolean isLumen) {
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

    @Override
    protected void generateModel() {
        DataProviders.MODEL_ITEM.getOrCreateModel(loc)
                .parent("minecraft:item/generated")
                .texture("layer0", Mod.modLoc(isLumen ? "paint_ball_shimmer" : "paint_ball"));
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

    private static ResourceLocation getLoc(boolean isLumen, AEColor color) {
        String name = color.id;
        if (isLumen) {
            name += "_lumen";
        }
        name += "_paint_ball";
        return new ResourceLocation(Mod.getModId(), name);
    }
}
