package com.stupidcoderx.common.datagen.model;

import net.minecraft.server.packs.PackType;

public record ResourceType(PackType type, String pathSuffix, String pathPrefix) {
    public static final ResourceType TEXTURE = new ResourceType(
            PackType.CLIENT_RESOURCES, ".png", "textures");
    public static final ResourceType MODEL = new ResourceType(
            PackType.CLIENT_RESOURCES, ".json", "models");
    public static final ResourceType MODEL_WITH_EXTENSION = new ResourceType(
            PackType.CLIENT_RESOURCES, "", "models");
}
