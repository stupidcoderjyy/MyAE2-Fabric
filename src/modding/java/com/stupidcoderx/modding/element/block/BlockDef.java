package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import com.stupidcoderx.modding.element.item.ItemDef;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class BlockDef<B extends Block> extends ItemDef<BlockItem> {
    private static final BlockPropertyManager bpManager = new BlockPropertyManager();
    public final B block;
    @DataGenOnly
    protected ModelBuilder mbBlock;

    public BlockDef(ResourceLocation loc, String name, B block, Item.Properties p) {
        super(loc, name, new BlockItem(block, p));
        this.block = block;
    }

    public BlockDef(String id, String name, B block, Item.Properties p) {
        this(Mod.modLoc(id), name, block, p);
    }

    public BlockDef(String id, String name, B block) {
        this(id, name, block, new Item.Properties());
    }

    public BlockDef(ResourceLocation loc, String name, B block) {
        this(loc, name, block, new Item.Properties());
    }

    public static BlockDef<Block> cubeAll(String id, String name) {
        return block(id, name, Block::new);
    }

    public static <T extends Block> BlockDef<Block> block(String id, String name, Function<BlockBehaviour.Properties, T> builder) {
        return new BlockDef<>(id, name, builder.apply(getPeekProp()));
    }

    public static WallBlockDef wall(String id, String name, BlockDef<?> baseBlock) {
        return new WallBlockDef(id, name, baseBlock);
    }

    public static StairsBlockDef stairs(String id, String name, BlockDef<?> baseBlock) {
        return new StairsBlockDef(id, name, baseBlock);
    }

    public static SlabBlockDef slab(String id, String name, BlockDef<?> baseBlock) {
        return new SlabBlockDef(id, name, baseBlock);
    }

    /**
     * 创建一个门方块。需要提供三个贴图，分别为：门的上半部分（id_top.png）、门的下半部分（id_bottom.png）、门的物品（id.png）
     * @param id 门方块id
     * @param name 门方块的默认名称
     * @param type 门的方块材料类型
     * @return 门方块容器
     */
    public static DoorBlockDef door(String id, String name, BlockSetType type) {
        return new DoorBlockDef(id, name, type);
    }

    protected static BlockBehaviour.Properties getPeekProp() {
        return bpManager.build();
    }

    @SafeVarargs
    public static void pushProp(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(false, false,modifiers);
    }

    @SafeVarargs
    public static void inheritProp(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(true, false, modifiers);
    }

    @SafeVarargs
    public static void pushPropDisposable(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(false, true,modifiers);
    }

    @SafeVarargs
    public static void inheritPropDisposable(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(true, true,modifiers);
    }

    public static void dupProp() {
        bpManager.dupProp();
    }

    public static void popProp() {
        bpManager.popProp();
    }

    @Override
    public void commonRegister() {
        super.commonRegister();
        Registry.register(BuiltInRegistries.BLOCK, loc, block);
    }

    @Override
    @DataGenOnly
    public void provideData() {
        super.provideData();
        provideBlockState();
    }

    @Override
    @DataGenOnly
    protected void provideModel() {
        mbBlock = provideBlockModel();
        provideItemModel();
    }

    @DataGenOnly
    protected void provideItemModel() {
        DataProviders.MODEL_ITEM.model(loc).parent(Mod.expandLoc("block", loc));
    }

    @DataGenOnly
    protected @Nullable ModelBuilder provideBlockModel() {
        return DataProviders.MODEL_BLOCK.model(loc)
                .parent("minecraft:block/cube_all")
                .texture("all", loc);
    }

    @DataGenOnly
    protected void provideBlockState() {
        if (block instanceof OrientableBlock ob) {
            ob.orientationStrategy().provideBlockState(loc);
        } else {
            DataProviders.BLOCK_STATE.variants(loc).condition(state -> new Model(loc));
        }
    }

    @Override
    @DataGenOnly
    protected String getTranslationKey() {
        return "block." + Mod.id() + "." + loc.getPath();
    }
}
