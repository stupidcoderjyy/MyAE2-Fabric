package com.stupidcoderx.modding.client.render;

import com.stupidcoderx.modding.core.IClientRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

@Environment(EnvType.CLIENT)
public abstract class ModTESR<E extends BlockEntity> implements BlockEntityRenderer<E>, IClientRegistry {
}
