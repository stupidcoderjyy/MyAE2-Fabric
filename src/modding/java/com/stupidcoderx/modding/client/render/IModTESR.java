package com.stupidcoderx.modding.client.render;

import com.stupidcoderx.modding.element.blockentity.TEDef;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IModTESR<E extends BlockEntity> extends BlockEntityRenderer<E> {
    static <T extends BlockEntity> void registerRenderer(TEDef<T> def, BlockEntityRendererProvider<T> provider) {
        BlockEntityRendererRegistry.register(def.type, provider);
    }
}
