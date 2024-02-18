package com.stupidcoderx.ae2.elements.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stupidcoderx.modding.client.render.ModTESR;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;

@Environment(EnvType.CLIENT)
public class SkyChestTESR extends ModTESR<SkyChestTE> {
    @Override
    public void render(SkyChestTE blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {

    }

    @Override
    public void clientRegister() {

    }
}
