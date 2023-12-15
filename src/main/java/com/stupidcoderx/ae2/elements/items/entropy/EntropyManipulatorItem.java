package com.stupidcoderx.ae2.elements.items.entropy;

import com.stupidcoderx.ae2.registry.AERecipes;
import com.stupidcoderx.modding.core.Mod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class EntropyManipulatorItem extends Item {
    private static final String CUR_POWER_NBT = "curPower";
    private static final int MAX_POWER = 50;
    private static final int BAR_COLOR = Mth.hsvToRgb(1.0f / 3.0f, 1.0f, 1.0f);

    public EntropyManipulatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return BAR_COLOR;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        double filled = getPower(itemStack) / (double)MAX_POWER;
        return Mth.clamp((int)(filled * 13), 0, 13);
    }

    //当点击一大片液体时，由于没有点到方块，故不会触发useOn，需要修改use的逻辑
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (result.getType() != HitResult.Type.BLOCK) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, player.getItemInHand(hand));
        }
        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!state.getFluidState().isEmpty() && Mod.allowInteract(level, pos, player)) {
            useOn(new UseOnContext(player, hand, result));
        }
        return new InteractionResultHolder<>(
                InteractionResult.sidedSuccess(level.isClientSide), player.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        Direction face = ctx.getClickedFace();
        int power = getPower(stack);
        //1)是否有足够的能量
        if (power == 0) {
            return InteractionResult.PASS;
        }
        //2)是否是真人玩家
        if (player == null) {
            return InteractionResult.FAIL;
        }
        //修正点击位置（使得流体可被识别）
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (result.getType() == HitResult.Type.BLOCK) {
            pos = result.getBlockPos();
        }
        //3)玩家是否可以使用物品
        if (!player.mayUseItemAt(pos, face, stack)) {
            return InteractionResult.FAIL;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        return tryApplyEffect(level, stack, pos, player, face) ?
                InteractionResult.CONSUME : InteractionResult.FAIL;
    }

    private boolean tryApplyEffect(Level level, ItemStack stack, BlockPos pos, Player player, Direction face) {
        Block block = level.getBlockState(pos).getBlock();
        boolean cool = player.isShiftKeyDown();
        if (tryApplyEntropyRecipes(cool, level, stack, block, pos, player)) {
            return true;
        }
        if (cool) {
            return false;
        }
        if (tryApplySmeltingRecipes(stack, level, player, pos, face)) {
            return true;
        }
        return trySpawnFire(level, stack, pos, face);
    }

    private boolean tryApplyEntropyRecipes(boolean cool, Level level, ItemStack stack, Block block, BlockPos pos, Player p) {
        EntropyRecipe recipe = findRecipe(cool, level, block, level.getFluidState(pos).getType());
        if (recipe == null) {
            return false;
        }
        extractPower(stack);
        recipe.apply(level, pos);
        return true;
    }

    private EntropyRecipe findRecipe(boolean isCool, Level level, Block block, Fluid fluid) {
        for (var entry : AERecipes.ENTROPY.getRegisteredRecipes(level).entrySet()) {
            if (entry.getValue().matches(isCool, block, fluid)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private boolean tryApplySmeltingRecipes(ItemStack heldItem, Level level, Player p, BlockPos pos, Direction d) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        //获取方块掉落物
        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, be, p, heldItem);

        Block outBlock = null;
        List<ItemStack> outItems = new ArrayList<>();

        //尝试对掉落物进行熔炼
        SimpleContainer tempInv = new SimpleContainer(1);
        for (ItemStack in : drops) {
            tempInv.setItem(0, in); //因为熔炉配方的matches方法检测的是容器第一个格，所以这么写
            var optional = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, tempInv, level);
            if (optional.isEmpty()) {
                return false;
            }
            ItemStack result = optional.get().assemble(tempInv, level.registryAccess());
            if (result.getItem() instanceof BlockItem) {
                Block candidate = Block.byItem(result.getItem());
                if (candidate == state.getBlock()) {
                    continue;  //排除石头、深板岩这种方块
                }
                if (outBlock == null) {
                    outBlock = candidate; //只有第一个方块才会放置
                    continue;
                }
            }
            outItems.add(result);
        }
        if (outBlock == null && outItems.isEmpty()) {
            return false;
        }
        extractPower(heldItem);
        Mod.playGlobalSound(level, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS,
                1.0F, 0.8F, 1.2F);
        if (outBlock == null) {
            outBlock = Blocks.AIR;
        }
        level.setBlockAndUpdate(pos, outBlock.defaultBlockState());
        Mod.spawnDropItems(level, pos, outItems);
        return true;
    }

    private boolean trySpawnFire(Level level, ItemStack item, BlockPos pos, Direction side) {
        pos = pos.relative(side);
        if (!BaseFireBlock.canBePlacedAt(level, pos, side) || !level.isEmptyBlock(pos)) {
            return false;
        }
        extractPower(item);
        Mod.playGlobalSound(level, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS,
                1.0F, 0.8F, 1.2F);
        level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        return true;
    }

    private void extractPower(ItemStack stack) {
        setPower(stack, getPower(stack) - 1);
    }

    private int getPower(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            return tag.getInt(CUR_POWER_NBT);
        }
        return MAX_POWER;
    }

    private void setPower(ItemStack stack, int p) {
        stack.getOrCreateTag().putInt(CUR_POWER_NBT, p);
    }
}
