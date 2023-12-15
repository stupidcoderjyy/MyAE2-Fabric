package com.stupidcoderx.modding.core;

import com.google.common.base.Stopwatch;
import com.stupidcoderx.modding.client.BuiltInModelRegistry;
import com.stupidcoderx.modding.datagen.recipe.RecipeDef;
import com.stupidcoderx.modding.element.BaseBlock;
import com.stupidcoderx.modding.element.ModCreativeTab;
import com.stupidcoderx.modding.element.item.ItemDef;
import com.stupidcoderx.modding.util.DimensionalBlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class Mod {
    public static final boolean isEnvDataGen = System.getProperty("fabric-api.datagen") != null;
    public static final boolean isEnvClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    protected static List<IRegistry> registries = new ArrayList<>();
    public static final ElementsRegistry<ItemDef<?,?>> ITEM_REGISTRY = create("item");
    public static final ElementsRegistry<ModCreativeTab> CREATIVE_TAB_REGISTRY = create("creativeTab");
    public static final ElementsRegistry<BaseBlock> BLOCK_REGISTRY = create("block");
    public static final ElementsRegistry<RecipeDef<?>> RECIPE_REGISTRY = create("recipe");
    private static Mod instance;

    public final String modId;
    private final Stopwatch watch = Stopwatch.createUnstarted();

    protected Mod(String modId) {
        this.modId = modId;
        instance = this;
        if (isEnvClient) {
            bootstrapClient();
        }
        watch.start();
        buildElements();
        ModLog.info("build elements: %s, took %dms", modId, watch.elapsed().toMillis());
        watch.reset();
        init();
        finishInit();
        ModLog.info("finish init: %s", modId);
    }

    protected abstract void buildElements();

    protected void init() {
        for (IRegistry r : registries) {
            watch.start();
            r.commonRegister();
            if (isEnvClient) {
                r.clientRegister();
            }
            ModLog.info("init: %s, took %dms", r.debugName(), watch.elapsed().toMillis());
            watch.reset();
        }
    }

    protected void finishInit() {
        for (IRegistry r : registries) {
            r.close();
        }
        registries = null;
    }

    protected void bootstrapClient() {
        registries.add(BuiltInModelRegistry.INSTANCE);
    }

    public static <T extends Mod> T getMod() {
        return (T)instance;
    }

    public static String getModId() {
        return instance.modId;
    }

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(getModId(), path);
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(path);
    }

    private static <T extends IRegistry> ElementsRegistry<T> create(String name) {
        ElementsRegistry<T> r = new ElementsRegistry<>(name);
        registries.add(r);
        return r;
    }

    /**
     *
     * @see #allowInteract(Level, BlockPos, Player)
     */
    public static boolean allowInteract(DimensionalBlockPos pos, Player player) {
        return pos.isInWorld(player.level()) && player.level().mayInteract(player, pos.getPos());
    }

    /**
     * 在一个世界内，玩家是否能在某个位置使用物品，即是否允许{@link net.minecraft.world.item.Item#useOn(UseOnContext)}被调用
     * @param level 世界
     * @param pos 位置
     * @param player 玩家
     * @return 能互动返回true
     */
    public static boolean allowInteract(Level level, BlockPos pos, Player player) {
        return level == player.level() && player.level().mayInteract(player, pos);
    }

    /**
     * 在服务端世界生成掉落物
     * @param level 生成世界
     * @param pos 生成位置
     * @param drops 要掉落的物品
     */
    public static void spawnDropItems(Level level, BlockPos pos, List<ItemStack> drops) {
        if (!level.isClientSide) {
            drops.forEach(stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack));
        }
    }

    /**
     * 在世界中播放声音
     * @param level 世界，必须为服务端世界，如果传入客户端世界则不会有任何操作
     * @param pos 播放位置
     * @param evt 声音
     * @param src 声音类源类型
     * @param volume 大小，正常为1.0
     * @param pitch 音高，MC中许多音效（如挖掘方块）通过随机音高实现
     */
    public static void playGlobalSound(Level level, BlockPos pos, SoundEvent evt, SoundSource src, float volume, float pitch) {
        if (!level.isClientSide) {
            level.playSound(null, pos, evt, src, volume, pitch);
        }
    }

    /**
     * 在世界中播放声音，声音具有范围内随机的音高
     * @param level 世界，必须为服务端世界，如果传入客户端世界则不会有任何操作
     * @param pos 播放位置
     * @param evt 声音
     * @param src 声音类源类型
     * @param volume 大小，正常为1.0
     * @param minPitch 最小音高
     * @param maxPitch 最大音高
     */
    public static void playGlobalSound(Level level, BlockPos pos, SoundEvent evt, SoundSource src, float volume, float minPitch, float maxPitch) {
        if (!level.isClientSide) {
            level.playSound(null, pos, evt, src, volume,
                    level.random.nextFloat() * (maxPitch - minPitch) + maxPitch);
        }
    }
}
