package com.stupidcoderx.ae2.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AEClient extends AE implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }
}
