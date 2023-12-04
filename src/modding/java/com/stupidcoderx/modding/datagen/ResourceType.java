package com.stupidcoderx.modding.datagen;

import net.minecraft.data.PackOutput;

public record ResourceType(PackOutput.Target type, String pathSuffix, String pathPrefix) {
    public static final ResourceType MODEL = new ResourceType(
            PackOutput.Target.RESOURCE_PACK, ".json", "models");
    public static final ResourceType BLOCK_STATE = new ResourceType(
            PackOutput.Target.RESOURCE_PACK, ".json", "blockstates");
}
