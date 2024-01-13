package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrapDoorBlockDef extends BlockDef<TrapDoorBlock>{
    public TrapDoorBlockDef(String id, String name, BlockSetType type) {
        super(id, name, new TrapDoorBlock(getPeekProp().noOcclusion(), type));
    }

    @Override
    @DataGenOnly
    public void provideData() {
        super.provideData();
        DataProviders.BLOCK_TAGS.of(BlockTags.TRAPDOORS).add(block);
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
                .variant("facing", List.of("north", "east", "south", "west"))
                .variant("half", List.of("bottom", "top"))
                .variantBool("open")
                .condition(state -> {
                    int y = state.optionIndex(0);   //facing
                    int x = 0;
                    boolean isTop = state.optionIndex(1) == 1;
                    boolean isOpen = state.optionIndex(2) == 1;
                    if (isTop && isOpen) {
                        x += 2;
                        y += 2;
                    }
                    String suffix;
                    if (isOpen) {
                        suffix = "_open";
                    } else if (isTop) {
                        suffix = "_top";
                    } else {
                        suffix = "_bottom";
                    }
                    return new Model(loc.withSuffix(suffix)).rotationX(x).rotationY(y);
                });
    }

    @Override
    @DataGenOnly
    protected void provideItemModel() {
        DataProviders.MODEL_ITEM.model(loc)
                .parent(loc.withPrefix("block/").withSuffix("_bottom"));
    }

    @Override
    @DataGenOnly
    protected @Nullable ModelBuilder provideBlockModel() {
        addModel("_top");
        addModel("_open");
        addModel("_bottom");
        return null;
    }

    private void addModel(String suffix) {
        DataProviders.MODEL_BLOCK.model(loc.withSuffix(suffix))
                .parent("block/template_orientable_trapdoor" + suffix)
                .texture("texture", loc);
    }
}
