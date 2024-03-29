package com.stupidcoderx.ae2.elements.items;

import com.stupidcoderx.ae2.util.AEColor;
import com.stupidcoderx.modding.core.IClientRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.element.item.ItemDef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;

public class PaintBallItemDef extends ItemDef<Item> implements IClientRegistry {
    public final boolean isLumen;
    public final AEColor color;

    private PaintBallItemDef(String name, boolean isLumen, AEColor color) {
        super(getLoc(isLumen, color), name, new Item(new Item.Properties()));
        this.isLumen = isLumen;
        this.color = color;
        Mod.addClientRegistry(this);
    }

    private static ResourceLocation getLoc(boolean isLumen, AEColor color) {
        String name = color.id;
        if (isLumen) {
            name += "_lumen";
        }
        name += "_paint_ball";
        return Mod.modLoc(name);
    }

    public static Map<AEColor, PaintBallItemDef> create(boolean isLumen, String name) {
        Map<AEColor, PaintBallItemDef> balls = new EnumMap<>(AEColor.class);
        for (AEColor c : AEColor.values()) {
            balls.put(c, new PaintBallItemDef(name, isLumen, c));
        }
        return balls;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientRegister() {
        ColorProviderRegistry.ITEM.register((stack, tint) -> calcColor(), this);
    }

    private int calcColor() {
        int rgb = color.rgbNormal;
        if (isLumen) {
            int r = rgb >> 16 & 0xff;
            int g = rgb >> 8 & 0xff;
            int b = rgb & 0xff;
            //略微提升亮度
            float fail = 0.7f;
            float full = 0xff * 0.3f;
            rgb = (int)(full + r * fail) << 16
                    | (int)(full + g * fail) << 8
                    | (int)(full + b * fail);
        }
        return rgb;
    }

    @Override
    protected void provideModel() {
        DataProviders.MODEL_ITEM.model(loc)
                .parent("item/generated")
                .texture("layer0", Mod.modLoc(isLumen ? "paint_ball_shimmer" : "paint_ball"));
    }

    @Override
    protected String getDefaultName() {
        return color.defaultName + this.defaultName;
    }
}
