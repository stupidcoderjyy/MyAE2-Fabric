package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.blocks.drive.DriveBlockDef;
import com.stupidcoderx.ae2.elements.blocks.glass.QuartzGlassBlockDef;
import com.stupidcoderx.modding.element.block.BlockDef;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Consumer;

public class AEBlocks {
    //Decorative
    public static final BlockDef<?> QUARTZ_BLOCK;
    public static final BlockDef<?> CUT_QUARTZ_BLOCK, CUT_QUARTZ_STAIRS, CUT_QUARTZ_WALL, CUT_QUARTZ_SLAB;
    public static final BlockDef<?> QUARTZ_DOOR, QUARTZ_TRAPDOOR, QUARTZ_FENCE, QUARTZ_FENCE_GATE;

    public static final BlockDef<?> QUARTZ_GLASS, QUARTZ_GLASS_VIBRANT;

    public static final BlockDef<?> DRIVE;
    static {
        BlockDef.pushTab(AECreativeTabs.MAIN);
        BlockDef.pushProp(Types.BASE);

        BlockDef.inheritProp(Types.QUARTZ);
        QUARTZ_BLOCK = BlockDef.cubeAll("quartz_block", "Certus石英块");
        CUT_QUARTZ_BLOCK = BlockDef.cubeAll("cut_quartz_block", "切制Certus石英块");
        CUT_QUARTZ_STAIRS = BlockDef.stairs("cut_quartz_stairs", "切制Certus楼梯", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_WALL = BlockDef.wall("cut_quartz_wall", "切制Certus墙", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_SLAB = BlockDef.slab("cut_quartz_slab", "切制Certus台阶", CUT_QUARTZ_BLOCK);
        QUARTZ_DOOR = BlockDef.door("quartz_door", "切制Certus门", BlockSetType.BAMBOO);
        QUARTZ_TRAPDOOR = BlockDef.trapDoor("quartz_trapdoor", "切制Certus活板门", BlockSetType.BAMBOO);
        QUARTZ_FENCE = BlockDef.fence("quartz_fence", "切制Certus栅栏", CUT_QUARTZ_BLOCK);
        QUARTZ_FENCE_GATE = BlockDef.fenceGate("quartz_fence_gate", "切制Certus栅栏门", WoodType.BAMBOO, CUT_QUARTZ_BLOCK);
        BlockDef.popProp();

        BlockDef.inheritProp(Types.GLASS);
        QUARTZ_GLASS = QuartzGlassBlockDef.create("石英玻璃", false);
        QUARTZ_GLASS_VIBRANT = QuartzGlassBlockDef.create("充能石英玻璃", true);
        BlockDef.popProp();

        BlockDef.inheritProp(Types.METAL);
        DRIVE = new DriveBlockDef("drive", "ME驱动器");
        BlockDef.popProp();

        BlockDef.popProp();
        BlockDef.popTab();
    }

    public static void build() {
    }

    private static class Types {
        public static final Consumer<Properties> BASE = p -> p.strength(2.2f, 11.f);
        public static final Consumer<Properties> QUARTZ = p -> p
                .strength(3.0f,5.0f)
                .mapColor(MapColor.QUARTZ)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops();

        public static final Consumer<Properties> GLASS = p -> p
                .mapColor(MapColor.NONE)
                .sound(SoundType.GLASS);

        public static final Consumer<Properties> METAL = p -> p
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .forceSolidOn();
    }
}
