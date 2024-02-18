package com.stupidcoderx.ae2.client.registry;

import com.stupidcoderx.ae2.client.tesr.SkyChestTESR;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AEBlockEntityRenderers {
    public static void register() {
        SkyChestTESR.register();
    }
}
