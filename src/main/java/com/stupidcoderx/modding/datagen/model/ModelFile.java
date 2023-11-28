package com.stupidcoderx.modding.datagen.model;

import net.minecraft.resources.ResourceLocation;

public class ModelFile {
    protected final ResourceLocation location;

    public ModelFile(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}

