package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.FenceBlock;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FenceBlockDef extends BlockDef<FenceBlock>{
    @DataGenOnly
    private Supplier<ResourceLocation> texture;

    public FenceBlockDef(String id, String name, BlockDef<?> textureBlock) {
        super(id, name, new FenceBlock(getPeekProp()));
        if (Mod.isEnvDataGen) {
            this.texture = () -> getTexture(textureBlock, "all");
        }
    }

    @Override
    @DataGenOnly
    public void provideData() {
        super.provideData();
        DataProviders.BLOCK_TAGS.of(BlockTags.FENCES).add(block);
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        ResourceLocation locPost = loc.withPrefix("block/").withSuffix("_post");
        ResourceLocation locSide = loc.withPrefix("block/").withSuffix("_side");
        var builder = DataProviders.BLOCK_STATE.multipart(loc)
                .apply(new Model(locPost));
        var faces = new String[]{"north", "east", "south", "west"};
        for(int y = 0 ; y < 4 ; y ++) {
            builder.apply(new Model(locSide).uvLock(true).rotationY(y))
                    .when(faces[y], "true");
        }
    }

    @Override
    @DataGenOnly
    protected void provideItemModel() {
        DataProviders.MODEL_ITEM.model(loc)
                .parent(loc.withPrefix("block/").withSuffix("_inventory"));
    }

    @Override
    @DataGenOnly
    protected @Nullable ModelBuilder provideBlockModel() {
        addModel("_post");
        addModel("_side");
        addModel("_inventory");
        return null;
    }

    private void addModel(String suffix) {
        DataProviders.MODEL_BLOCK.model(loc.withSuffix(suffix))
                .parent("block/fence" + suffix)
                .texture("texture", texture.get());
    }
}
