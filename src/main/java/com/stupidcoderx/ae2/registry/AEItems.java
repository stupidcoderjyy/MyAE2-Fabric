package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.items.PaintBallItemDef;
import com.stupidcoderx.ae2.elements.items.compass.CompassItemDef;
import com.stupidcoderx.ae2.elements.items.entropy.EntropyManipulatorItem;
import com.stupidcoderx.ae2.util.AEColor;
import com.stupidcoderx.modding.element.item.SimpleItemDef;
import com.stupidcoderx.modding.element.item.ToolItemDef;
import com.stupidcoderx.modding.element.item.ToolType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

import java.util.Map;
import java.util.function.Function;

public class AEItems {
    public static final SimpleItemDef<Item> SILICON, FLUIX_CRYSTAL;
    public static final CompassItemDef METEORITE_COMPASS;
    public static final Map<AEColor, PaintBallItemDef> PAINT_BALLS, LUMEN_PAINT_BALLS;

    public static final ToolItemDef<?> FLUIX_AXE;
    public static final ToolItemDef<?> ENTROPY_MANIPULATOR;

    static {
        SILICON = SimpleItemDef.create("silicon", AECreativeTabs.MAIN);
        FLUIX_CRYSTAL = SimpleItemDef.create("fluix_crystal", AECreativeTabs.MAIN);
        PAINT_BALLS = PaintBallItemDef.create(false);
        LUMEN_PAINT_BALLS = PaintBallItemDef.create(true);
        METEORITE_COMPASS = new CompassItemDef(AECreativeTabs.MAIN);

        FLUIX_AXE = tool("fluix_axe", p -> new AxeItem(ToolTypes.FLUIX, 6.0f, -3.1f, p));
        ENTROPY_MANIPULATOR = tool("entropy_manipulator", p -> new EntropyManipulatorItem(p.stacksTo(1)));
    }

    private static <T extends Item> ToolItemDef<T> tool(String id, Function<Item.Properties, T> itemBuilder) {
        T item = itemBuilder.apply(new Item.Properties());
        return new ToolItemDef<>(id, item).creativeTab(AECreativeTabs.TOOLS);
    }

    private static class ToolTypes{
        public static final ToolType FLUIX;

        static {
            FLUIX = new ToolType(Tiers.IRON).repairIngredient(() -> AEItems.FLUIX_CRYSTAL);
        }
    }

    public static void build() {
    }
}
