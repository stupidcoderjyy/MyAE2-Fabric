package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.items.PaintBallItemDef;
import com.stupidcoderx.ae2.elements.items.compass.CompassItemDef;
import com.stupidcoderx.ae2.elements.items.entropy.EntropyManipulatorItem;
import com.stupidcoderx.ae2.elements.items.vanillatools.FluixAxe;
import com.stupidcoderx.ae2.elements.items.vanillatools.FluixPickaxe;
import com.stupidcoderx.ae2.util.AEColor;
import com.stupidcoderx.modding.element.item.ItemDef;
import net.minecraft.world.item.CreativeModeTabs;

import java.util.Map;

public class AEItems {
    public static final ItemDef<?> SILICON, FLUIX_CRYSTAL;
    public static final CompassItemDef METEORITE_COMPASS;
    public static final Map<AEColor, PaintBallItemDef> PAINT_BALLS, LUMEN_PAINT_BALLS;

    public static final ItemDef<?> FLUIX_AXE, FLUIX_PICKAXE;
    public static final ItemDef<?> ENTROPY_MANIPULATOR;

    static {
        ItemDef.pushTab(AECreativeTabs.MAIN);
        SILICON = ItemDef.simple("silicon", "硅");
        FLUIX_CRYSTAL = ItemDef.simple("fluix_crystal", "Fluix水晶");
        PAINT_BALLS = PaintBallItemDef.create(false, "染色球");
        LUMEN_PAINT_BALLS = PaintBallItemDef.create(true, "光通染色球");
        METEORITE_COMPASS = new CompassItemDef("陨石罗盘");

        ItemDef.pushTab(CreativeModeTabs.TOOLS_AND_UTILITIES);
        FLUIX_AXE = ItemDef.tool("fluix_axe", "Fluix斧头", p -> new FluixAxe( 6.0f, -3.1f, p));
        FLUIX_PICKAXE = ItemDef.tool("fluix_pickaxe", "Fluix镐子", p -> new FluixPickaxe(1, -2.8F, p));
        ItemDef.popTab();

        ENTROPY_MANIPULATOR = ItemDef.tool("entropy_manipulator", "熵变机械臂", p -> new EntropyManipulatorItem(p.stacksTo(1)));
        ItemDef.popTab();
    }

    public static void build() {
    }
}
