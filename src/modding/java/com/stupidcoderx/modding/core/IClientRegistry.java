package com.stupidcoderx.modding.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface IClientRegistry extends IRegistryComponent{
    @Environment(EnvType.CLIENT)
    void clientRegister();
}
