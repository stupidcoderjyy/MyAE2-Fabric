package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.modding.element.block.BlockDef;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class AEBlocks {
    //Decorative
    public static final BlockDef<?> QUARTZ_BLOCK;
    public static final BlockDef<?> CUT_QUARTZ_BLOCK, CUT_QUARTZ_STAIRS, CUT_QUARTZ_WALL, CUT_QUARTZ_SLAB;

    static {
        BlockDef.pushTab(AECreativeTabs.MAIN);
        BlockDef.pushProperties(Types.QUARTZ);
        QUARTZ_BLOCK = BlockDef.cubeAll("quartz_block", "Certus石英块");
        CUT_QUARTZ_BLOCK = BlockDef.cubeAll("cut_quartz_block", "切制Certus石英块");
        CUT_QUARTZ_STAIRS = BlockDef.stairs("cut_quartz_stairs", "切制Certus楼梯", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_WALL = BlockDef.wall("cut_quartz_wall", "切制Certus墙", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_SLAB = BlockDef.slab("cut_quartz_slab", "切制Certus台阶", CUT_QUARTZ_BLOCK);
        BlockDef.popTab();
        BlockDef.popProperties();
    }

    public static void build() {
    }

    private static class Types {
        public static final Properties QUARTZ = Properties.of()
                .strength(3.0f,5.0f)
                .mapColor(MapColor.STONE)
                .sound(SoundType.STONE)
                .forceSolidOn()
                .requiresCorrectToolForDrops();
    }
}
