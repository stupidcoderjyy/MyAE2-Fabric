/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package com.stupidcoderx.modding.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IModCustomModel extends UnbakedModel, BakedModel, FabricBakedModel{
    /**
     * @return 本模型（以及其模型依赖，依赖的依赖，等）依赖的纹理
     */
    @Override
    default Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }


    @Override
    default void resolveParents(Function<ResourceLocation, UnbakedModel> function) {
        for (ResourceLocation dependency : getDependencies()) {
            function.apply(dependency).resolveParents(function);
        }
    }

    /**
     * 如果添加了钠模组，则必须同时添加铟模组，否则此方法无效
     * @see FabricBakedModel#emitItemQuads(ItemStack, Supplier, RenderContext)
     * @see #getOverrides()
     */
    @Override
    default void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
    }

    /**
     * 如果添加了钠模组，则必须同时添加铟模组，否则此方法无效
     * @see FabricBakedModel#emitBlockQuads(BlockAndTintGetter, BlockState, BlockPos, Supplier, RenderContext)
     */
    @Override
    default void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
    }

    /**
     * 如果添加了钠模组，则必须同时添加铟模组，否则此方法无效
     * @return 使用FabricApi进行加载时，返回false
     */
    @Override
    default boolean isVanillaAdapter() {
        return false;
    }

    /**
     * 在{@link #isVanillaAdapter()}返回false的情况下，这个方法不会被调用，暂时不用管
     */
    @Override
    default List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    /**
     *
     * @return 是否使用环境光遮蔽
     */
    @Override
    default boolean useAmbientOcclusion() {
        return false;
    }

    /**
     * @return 若当前模型是3D模型且需要在UI界面渲染，返回true（如陨石罗盘）
     */
    @Override
    default boolean isGui3d() {
        return false;
    }

    /**
     * @return 在UI界面渲染当前3D模型时是否模拟光照
     */
    @Override
    default boolean usesBlockLight() {
        return false;
    }

    /**
     * 将其渲染为物品时，返回true将导致模型不被渲染，转而回到
     * {@link net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer#renderByItem}。
     * 对于某些原版物品，如箱子和旗帜，此方法被硬编码为将数据从物品复制到BlockEntity中，
     * 然后使用{@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer}
     * 来渲染BE以代替物品
     * @return 不知道有啥用，一般都返回false
     * @see net.minecraft.client.renderer.entity.ItemRenderer#render(ItemStack, ItemDisplayContext, boolean, PoseStack, MultiBufferSource, int, int, BakedModel)
     */
    @Override
    default boolean isCustomRenderer() {
        return false;
    }

    /**
     * 获得一个物品模型重载对象，通过它能够根据物品、世界、实体等的信息更改物品渲染的模型。
     * 这个方法被内置于{@link net.minecraft.client.renderer.entity.ItemRenderer#getModel(ItemStack, Level, LivingEntity, int)}中
     * 在渲染物品时，会先获取物品的模型，再执行渲染，也就是{@link #emitItemQuads(ItemStack, Supplier, RenderContext)}
     * @return 返回要用于此模型的物品重载，仅当此模型被渲染为物品时才使用此选项
     * @see net.minecraft.client.gui.GuiGraphics#renderItem(ItemStack, int, int)
     */
    @Override
    ItemOverrides getOverrides();

    /**
     * @return 获得模型使用的变换（如平移、旋转、缩放），模型文件里面的变换这是这个
     *
     */
    @Override
    ItemTransforms getTransforms();

    /**
     * @return 粒子应使用的任何纹理。对于方块，它将在实体掉落在其上或其被破坏时显示。
     * 对于物品，它将在报废或被吃掉时显示
     */
    @Override
    TextureAtlasSprite getParticleIcon();
}
