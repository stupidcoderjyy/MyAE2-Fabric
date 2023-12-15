package com.stupidcoderx.modding.core;

import com.google.common.base.Stopwatch;
import com.mojang.authlib.GameProfile;
import com.stupidcoderx.modding.datagen.recipe.RecipeDef;
import com.stupidcoderx.modding.element.BaseBlock;
import com.stupidcoderx.modding.element.ModCreativeTab;
import com.stupidcoderx.modding.element.item.ItemDef;
import com.stupidcoderx.modding.util.DimensionalBlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static boolean allowInteract(DimensionalBlockPos pos, Player player) {
        return pos.isInWorld(player.level()) && player.level().mayInteract(player, pos.getPos());
    }

    /**
     * 在一个世界内，玩家是否能在某个位置进行互动。如玩家无法攻击处于出生保护的玩家
     * @param level 世界
     * @param pos 位置
     * @param player 玩家
     * @return 能互动返回true
     */
    public static boolean allowInteract(Level level, BlockPos pos, Player player) {
        return level == player.level() && player.level().mayInteract(player, pos);
    }

    /**
     * 在一个世界中获得或创建一个假人
     * @param level 世界
     * @param playerUuid 假人ID
     * @return 假人对象
     */
    public static Player getFakePlayer(@NotNull ServerLevel level, @Nullable UUID playerUuid) {
        if (playerUuid == null) {
            playerUuid = FakePlayer.DEFAULT_UUID;
        }
        return FakePlayer.get(level, new GameProfile(playerUuid, "[" + Mod.getModId().toUpperCase() + "]"));
    }

    /**
     * 生成掉落物
     * @param level 生成世界
     * @param pos 生成位置
     * @param drops 要掉落的物品
     */
    public static void spawnDropItems(Level level, BlockPos pos, List<ItemStack> drops) {
        if (!level.isClientSide) {
            drops.forEach(stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack));
        }
    }
}
