package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ButtonBlockDef extends BlockDef<ButtonBlock>{
    @DataGenOnly
    private Supplier<ResourceLocation> texture;

    public ButtonBlockDef(String id, String name, BlockSetType type, BlockDef<?> textureBlock, int ticksToStayPressed, boolean arrowCanPress) {
        super(id, name, new ButtonBlock(getPeekProp(), type, ticksToStayPressed, arrowCanPress));
        if (Mod.isEnvDataGen) {
            this.texture = () -> getTexture(textureBlock, "all");
        }
    }

    @Override
    @DataGenOnly
    public void provideData() {
        super.provideData();
        DataProviders.BLOCK_TAGS.of(BlockTags.BUTTONS).add(block);
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc)
                .variant("facing", List.of("north", "east", "south", "west"))
                .variant("face", List.of("floor", "wall", "ceiling"))
                .variantBool("powered")
                .condition(state -> {
                    int x = 0, y = 0;
                    switch (state.optionIndex(1)) { //face
                        case 0 -> {}        //floor
                        case 1 -> x = 1;    //wall
                        case 2 -> {         //ceiling
                            x = 2;
                            y = 2;
                        }
                    }
                    y += state.optionIndex(0); //facing
                    var model = state.optionIndex(2) > 0 ?
                            loc.withSuffix("_pressed") : loc;
                    return new Model(model).rotationX(x).rotationY(y);
                });
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
        addModel("");
        addModel("_pressed");
        addModel("_inventory");
        return null;
    }

    private void addModel(String suffix) {
        DataProviders.MODEL_BLOCK.model(loc.withSuffix(suffix))
                .parent("block/button" + suffix)
                .texture("texture", texture.get());
    }
}
