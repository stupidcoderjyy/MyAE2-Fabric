package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public abstract class ExtendedBlockDef<B extends Block> extends BlockDef<B>{
    protected final BlockDef<?> baseBlock;

    public ExtendedBlockDef(String id, String name, BlockDef<?> baseBlock, B block) {
        super(id, name, block);
        this.baseBlock = baseBlock;
    }

    @DataGenOnly
    protected ResourceLocation getBaseBlockTexture(String key, String ... candidates) {
        ResourceLocation loc;
        for (String c : candidates) {
            loc = baseBlock.mbBlock.textures.get(c);
            if (loc != null) {
                return loc;
            }
        }
        throw new IllegalStateException("failed to get texture \"" + key + "\" for model:" + baseBlock.mbBlock);
    }
}
