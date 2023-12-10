package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.items.EntropyManipulatorItem;
import com.stupidcoderx.ae2.elements.items.PaintBallItemDef;
import com.stupidcoderx.modding.element.item.SimpleItemDef;
import com.stupidcoderx.modding.element.item.ToolItemDef;
import com.stupidcoderx.modding.element.item.ToolType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

import java.util.function.Function;

public class AEItems {
    public static final SimpleItemDef<Item> SILICON;

    public static final ToolItemDef<?> FLUIX_AXE;
    public static final ToolItemDef<?> ENTROPY_MANIPULATOR;

    static {
        SILICON = SimpleItemDef.create("silicon", AECreativeTabs.MAIN);

        FLUIX_AXE = tool("fluix_axe", p -> new AxeItem(ToolTypes.TOOL_TYPE_FLUIX, 6.0f, -3.1f, p));
        ENTROPY_MANIPULATOR = tool("entropy_manipulator", p -> new EntropyManipulatorItem(p.stacksTo(1)));
        PaintBallItemDef.create();
    }

    private static <T extends Item> ToolItemDef<T> tool(String id, Function<Item.Properties, T> itemBuilder) {
        T item = itemBuilder.apply(new Item.Properties());
        return new ToolItemDef<>(id, item).creativeTab(AECreativeTabs.TOOLS);
    }

    private static class ToolTypes{
        public static final ToolType TOOL_TYPE_FLUIX;

        static {
            TOOL_TYPE_FLUIX = new ToolType(Tiers.IRON).repairIngredient(() -> AEItems.SILICON);
        }
    }

    public static void build() {
    }
}
