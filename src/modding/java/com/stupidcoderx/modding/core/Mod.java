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

/**
 * 模组主类，可以继承并实现{@link net.fabricmc.api.ClientModInitializer}或者{@link net.fabricmc.api.DedicatedServerModInitializer}
 * 作为客户端和服务端启动点
 */
public abstract class Mod {
    protected static List<IRegistry> registries = new ArrayList<>();
    /**
     * 模组是否运行在数据生成环境中
     */
    public static final boolean isEnvDataGen = System.getProperty("fabric-api.datagen") != null;
    /**
     * 模组是否运行在客户端环境下
     */
    public static final boolean isEnvClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    /**
     * 负责注册一系列{@link ModCreativeTab}<p>
     * 所有{@link ModCreativeTab}在初始化过程中会自动注册进此对象
     */
    public static final RegistryList<ModCreativeTab> CREATIVE_TAB_REGISTRY = create("creativeTab");
    /**
     * 负责注册一系列{@link ItemDef}<p>
     * 所有{@link ItemDef}在初始化过程中会自动注册进此对象
     */
    public static final RegistryList<ItemDef<?>> ITEM_REGISTRY = create("item");
    public static final RegistryList<BaseBlock> BLOCK_REGISTRY = create("block");
    public static final RegistryList<RecipeDef<?>> RECIPE_REGISTRY = create("recipe");
    private static Mod instance;
    public final String modId;

    /**
     * 模组主类构造器
     * @param modId 模组id，需要和fabric.mod.json保持一致
     */
    protected Mod(String modId) {
        this.modId = modId;
        instance = this;
        if (isEnvClient) {
            bootstrapClient();
        }
        ModLog.info("begin init: %s, registries: %s", modId, registries);
        Stopwatch watch = Stopwatch.createUnstarted();
        watch.start();
        buildElements();
        init();
        finishInit();
        ModLog.info("finish init: %s, took %dms", modId, watch.elapsed().toMillis());
    }

    /**
     * 模组对象构造和注册逻辑（注册进{@link #registries}）
     */
    protected abstract void buildElements();

    /**
     * 模组对象调用MC内部的API进行注册的逻辑
     */
    protected void init() {
        for (IRegistry r : registries) {
            r.commonRegister();
            if (isEnvClient) {
                r.clientRegister();
            }
        }
    }

    /**
     * 模组加载结束，释放资源
     */
    protected void finishInit() {
        for (IRegistry r : registries) {
            r.close();
        }
        registries = null;
    }

    protected void bootstrapClient() {
        registries.add(BuiltInModelRegistry.INSTANCE);
    }

    /**
     * @return 模组id
     */
    public static String id() {
        return instance.modId;
    }

    /**
     * @return 模组资源路径
     */
    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(id(), path);
    }

    /**
     * @return 原版资源路径
     */
    public static ResourceLocation loc(String path) {
        return new ResourceLocation(path);
    }

    private static <T extends IRegistry> RegistryList<T> create(String name) {
        RegistryList<T> r = new RegistryList<>(name);
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
     * @param keepStack 是否保留物品，如果传入false，则原先的ItemStack会被销毁
     */
    public static void spawnDropItems(Level level, BlockPos pos, List<ItemStack> drops, boolean keepStack) {
        if (!level.isClientSide) {
            drops.forEach(stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    keepStack ? stack.copy() : stack));
        }
    }

    /**
     * @see #spawnDropItems(Level, BlockPos, List, boolean)
     */
    public static void spawnDropItems(Level level, BlockPos pos, List<ItemStack> drops) {
        spawnDropItems(level, pos, drops, false);
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
