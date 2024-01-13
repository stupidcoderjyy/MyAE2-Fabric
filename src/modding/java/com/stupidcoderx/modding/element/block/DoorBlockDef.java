package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DoorBlockDef extends BlockDef<DoorBlock>{
    public DoorBlockDef(String id, String name, BlockSetType type) {
        super(id, name, new DoorBlock(getPeekProp(), type));
    }

    @Override
    @DataGenOnly
    public void provideData() {
        super.provideData();
        DataProviders.BLOCK_TAGS.of(BlockTags.DOORS).add(block);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientRegister() {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc)
                .variant("facing", List.of("east", "south", "west", "north"))
                .variant("half", List.of("lower", "upper"))
                .variant("hinge", List.of("left", "right"))
                .variantBool("open")
                .condition(state -> {
                    int y = state.optionIndex(0);   //facing
                    boolean open = state.optionIndex(3) == 1;
                    if (open) {    //open == true
                        y ++;
                        if (state.optionIndex(2) == 1) {    //hinge == right
                            y += 2;
                        }
                    }
                    String modelPath = "block/" + loc.getPath();
                    modelPath += state.optionIndex(1) == 0 ? "_bottom" : "_top";    //half
                    modelPath += "_" + state.option(2);   //hinge
                    if (open) {
                        modelPath += "_open";
                    }
                    return new Model(Mod.modLoc(modelPath)).rotationY(y);
                });
    }

    @Override
    @DataGenOnly
    protected void provideItemModel() {
        DataProviders.MODEL_ITEM.model(loc)
                .parent("minecraft:item/generated")
                .texture("layer0", loc);
    }

    @Override
    @DataGenOnly
    protected @Nullable ModelBuilder provideBlockModel() {
        for (int i = 0 ; i < 8 ; i ++) {
            String suffix = i >> 2 == 0 ? "_bottom" : "_top";
            suffix += ((i >> 1) & 1) == 0 ? "_left" : "_right";
            if ((i & 1) == 1) {
                suffix += "_open";
            }
            DataProviders.MODEL_BLOCK.model(loc.withSuffix(suffix))
                    .parent(Mod.loc("block/door" + suffix))
                    .texture("bottom", loc.withSuffix("_bottom"))
                    .texture("top", loc.withSuffix("_top"));
        }
        return null;
    }
}
