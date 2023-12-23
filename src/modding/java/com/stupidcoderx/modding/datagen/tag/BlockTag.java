package com.stupidcoderx.modding.datagen.tag;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.world.level.block.Block;

public class BlockTag extends Tag<BlockTag, Block> {
    @Override
    JsonElement serialize(Block block) {
        return new JsonPrimitive(block.builtInRegistryHolder().key().location().toString());
    }
}
