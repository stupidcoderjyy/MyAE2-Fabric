package com.stupidcoderx.modding.datagen;

import net.minecraft.data.PackOutput;

public enum ResourceType {
    MODEL(PackOutput.Target.RESOURCE_PACK, ".json", "models"),
    BLOCK_STATE(PackOutput.Target.RESOURCE_PACK, ".json", "blockstates"),
    RECIPE(PackOutput.Target.DATA_PACK, ".json", "recipes")
    ;
    public final PackOutput.Target type;
    public final String pathSuffix;
    public final String pathPrefix;

    ResourceType(PackOutput.Target type, String pathSuffix, String pathPrefix) {
        this.type = type;
        this.pathSuffix = pathSuffix;
        this.pathPrefix = pathPrefix;
    }
}
