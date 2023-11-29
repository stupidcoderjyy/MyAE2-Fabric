package com.stupidcoderx.ae2.core.registry;

import com.stupidcoderx.ae2.blocks.ChairBlock;
import com.stupidcoderx.modding.element.BaseBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class AEBlocks {
    //Block Properties
    public static final Properties PROP_QUARTZ = Properties.of()
            .strength(3.0f,5.0f)
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .forceSolidOn()
            .requiresCorrectToolForDrops();

    //Decorative
    public static final BaseBlock QUARTZ_BLOCK = simple(PROP_QUARTZ, "quartz_block");
    public static final ChairBlock CHAIR = new ChairBlock(PROP_QUARTZ, "chair");

    private static BaseBlock simple(Properties p, String id) {
        return new BaseBlock(p, id).creativeTab(AECreativeTabs.MAIN);
    }

    public static void build() {
        //load class
    }
}
