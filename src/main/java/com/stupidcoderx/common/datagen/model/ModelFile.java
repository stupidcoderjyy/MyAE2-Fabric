package com.stupidcoderx.common.datagen.model;

import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;

public abstract class ModelFile {
    protected final ResourceLocation location;

    public ModelFile(ResourceLocation location) {
        this.location = location;
    }

    protected boolean exists() {
        return true;
    }

    public void ensureExistence() {
        Preconditions.checkState(exists(), "Model at %s doesn't exist", location);
    }

    @Override
    public String toString() {
        return location.toString();
    }
}

