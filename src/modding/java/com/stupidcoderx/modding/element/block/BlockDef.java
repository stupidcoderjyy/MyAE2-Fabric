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
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;
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

    /**
     * 创建一个墙方块，不需要提供材质文件
     * @param id 方块id
     * @param name 方块的默认名称
     * @param baseBlock 提供材质的方块，方块模型必须使用生成器，需要提供侧面的材质。侧面材质接受"side"或"all"
     * @return 墙方块容器
     */
    public static WallBlockDef wall(String id, String name, BlockDef<?> baseBlock) {
        return new WallBlockDef(id, name, baseBlock);
    }

    /**
     * 创建一个楼梯方块，不需要提供材质文件
     * @param id 方块id
     * @param name 方块的默认名称
     * @param baseBlock 提供材质的方块，方块模型必须使用生成器，需要提供顶部、侧面和底部的材质。顶部材质接受"top"、"end"或"all"；侧面材质
     *                  接受"side"或"all"；底部材质接受"bottom"、"end"或"all"
     * @return 楼梯方块容器
     */
    public static StairsBlockDef stairs(String id, String name, BlockDef<?> baseBlock) {
        return new StairsBlockDef(id, name, baseBlock);
    }

    /**
     * 创建一个台阶方块，不需要提供材质文件
     * @param id 方块id
     * @param name 方块的默认名称
     * @param baseBlock 提供材质的方块，方块模型必须使用生成器，需要提供顶部、侧面和底部的材质。顶部材质接受"top"、"end"或"all"；侧面材质
     *                  接受"side"或"all"；底部材质接受"bottom"、"end"或"all"
     * @return 台阶方块
     */
    public static SlabBlockDef slab(String id, String name, BlockDef<?> baseBlock) {
        return new SlabBlockDef(id, name, baseBlock);
    }

    /**
     * 创建一个门方块。需要提供三个贴图，分别为：门的上半部分（[id]_top.png）、门的下半部分（[id]_bottom.png）、门的物品（[id].png）
     * @param id 方块id
     * @param name 方块的默认名称
     * @param type 方块材料类型
     * @return 门方块容器
     */
    public static DoorBlockDef door(String id, String name, BlockSetType type) {
        return new DoorBlockDef(id, name, type);
    }

    /**
     * 创建一个活板门方块。需要提供活板门方块贴图（id.png）
     * @param id 方块id
     * @param name 方块的默认名称
     * @param type 方块材料类型
     * @return 活板门方块
     */
    public static TrapDoorBlockDef trapDoor(String id, String name, BlockSetType type) {
        return new TrapDoorBlockDef(id, name, type);
    }

    /**
     * 创建一个栅栏方块，不需要提供材质文件
     * @param id 方块id
     * @param name 方块的默认名称
     * @param texture 提供材质的方块，方块模型必须使用生成器，且使用了名为"all"的材质
     * @return 栅栏方块
     */
    public static FenceBlockDef fence(String id, String name, BlockDef<?> texture) {
        return new FenceBlockDef(id, name, texture);
    }

    /**
     * 创建一个栅栏门方块，不需要提供材质文件
     * @param id 方块id
     * @param name 方块的默认名称
     * @param type 木头类型，这决定了打开栅栏门的声音
     * @param texture 提供材质的方块，方块模型必须使用生成器，且使用了名为"all"的材质
     * @return 栅栏门方块
     */
    public static FenceGateBlockDef fenceGate(String id, String name, WoodType type, BlockDef<?> texture) {
        return new FenceGateBlockDef(id, name, type, texture);
    }

    /**
     * 创建一个按钮方块，不需要提供材质文件
     * @param id 方块id
     * @param name 方块的默认名称
     * @param type 方块材料类型
     * @param texture 提供材质的方块，方块模型必须使用生成器，且使用了名为"all"的材质
     * @param ticksToStayPressed 激活按钮后，按钮保持激活状态的时间
     * @param arrowCanPress 箭矢是否能激活按钮
     * @return 按钮方块
     */
    public static ButtonBlockDef button(String id, String name, BlockSetType type, BlockDef<?> texture,
                                        int ticksToStayPressed, boolean arrowCanPress) {
        return new ButtonBlockDef(id, name, type, texture, ticksToStayPressed, arrowCanPress);
    }

    /**
     *
     * @see #button(String, String, BlockSetType, BlockDef, int, boolean)
     */
    public static ButtonBlockDef button(String id, String name, BlockSetType type, BlockDef<?> texture) {
        return new ButtonBlockDef(id, name, type, texture, 20, true);
    }

    protected static BlockBehaviour.Properties getPeekProp() {
        return bpManager.build();
    }

    @SafeVarargs
    public static void pushProp(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(false, false, modifiers);
    }

    @SafeVarargs
    public static void inheritProp(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(true, false, modifiers);
    }

    @SafeVarargs
    public static void pushPropDisposable(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(false, true, modifiers);
    }

    @SafeVarargs
    public static void inheritPropDisposable(Consumer<BlockBehaviour.Properties> ... modifiers) {
        bpManager.pushProp(true, true, modifiers);
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

    @DataGenOnly
    protected static ResourceLocation getTexture(@NotNull BlockDef<?> def, String ... textureKeys) {
        if (def.mbBlock == null) {
            return def.loc;
        }
        for (String key : textureKeys) {
            var res = def.mbBlock.textures.get(key);
            if (res != null) {
                return res;
            }
        }
        return def.loc;
    }
}
