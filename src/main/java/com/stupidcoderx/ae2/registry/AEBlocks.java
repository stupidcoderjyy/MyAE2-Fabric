package com.stupidcoderx.ae2.registry;

import com.stupidcoderx.ae2.elements.blocks.DisplayBlockDef;
import com.stupidcoderx.ae2.elements.blocks.drive.DriveBlockDef;
import com.stupidcoderx.ae2.elements.blocks.glass.QuartzGlassBlockDef;
import com.stupidcoderx.modding.element.block.BlockDef;
import com.stupidcoderx.modding.element.block.EntityBlockDef;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Consumer;

public class AEBlocks {
    //Decorative
    public static final BlockDef<?> QUARTZ_BLOCK, SKY_STONE;
    public static final BlockDef<?> CUT_QUARTZ_BLOCK, CUT_QUARTZ_STAIRS, CUT_QUARTZ_SLAB,
            CUT_QUARTZ_WALL, CUT_QUARTZ_FENCE, CUT_QUARTZ_FENCE_GATE, CUT_QUARTZ_BUTTON,
            CUT_QUARTZ_TRAPDOOR, CUT_QUARTZ_DOOR;
    public static final BlockDef<?> QUARTZ_GLASS, QUARTZ_GLASS_VIBRANT;
    public static final BlockDef<?> DRIVE;
    public static final EntityBlockDef<?> SKY_STONE_CHEST;
    static {
        BlockDef.pushProp(Types.BASE);
        BlockDef.inheritProp(Types.QUARTZ);
        QUARTZ_BLOCK = BlockDef.cubeAll("quartz_block", "赛特斯石英块");
        SKY_STONE = BlockDef.cubeAll("sky_stone", "陨石");
        CUT_QUARTZ_BLOCK = BlockDef.cubeAll("cut_quartz_block", "切制赛特斯石英块");
        CUT_QUARTZ_STAIRS = BlockDef.stairs("cut_quartz_stairs", "切制赛特斯石英楼梯", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_SLAB = BlockDef.slab("cut_quartz_slab", "切制赛特斯石英台阶", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_WALL = BlockDef.wall("cut_quartz_wall", "切制赛特斯石英墙", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_FENCE = BlockDef.fence("cut_quartz_fence", "切制赛特斯石英栅栏", CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_FENCE_GATE = BlockDef.fenceGate("cut_quartz_fence_gate", "切制赛特斯石英栅栏门", WoodType.BAMBOO, CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_BUTTON = BlockDef.button("cut_quartz_button", "切制赛特斯石英按钮", BlockSetType.BAMBOO, CUT_QUARTZ_BLOCK);
        CUT_QUARTZ_TRAPDOOR = BlockDef.trapDoor("cut_quartz_trapdoor", "切制赛特斯石英活板门", BlockSetType.BAMBOO);
        CUT_QUARTZ_DOOR = BlockDef.door("cut_quartz_door", "切制赛特斯石英门", BlockSetType.BAMBOO);
        BlockDef.popProp();

        BlockDef.inheritProp(Types.GLASS);
        QUARTZ_GLASS = QuartzGlassBlockDef.create("石英玻璃", false);
        QUARTZ_GLASS_VIBRANT = QuartzGlassBlockDef.create("充能石英玻璃", true);
        BlockDef.popProp();

        BlockDef.inheritProp(Types.METAL);
        DRIVE = new DriveBlockDef("drive", "ME驱动器");
        BlockDef.popProp();

        BlockDef.inheritProp(Types.STONE);
        SKY_STONE_CHEST = new DisplayBlockDef("sky_stone_chest", "陨石箱子");
        BlockDef.popProp();
        BlockDef.popProp();
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

        public static final Consumer<Properties> STONE = p -> p
                .mapColor(MapColor.STONE)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops();
    }
}
