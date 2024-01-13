package com.stupidcoderx.modding.element.block;

import com.stupidcoderx.modding.core.DataGenOnly;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.blockstate.multipart.MultiPartBlockStateBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.WallBlock;

import java.util.function.Supplier;

public class WallBlockDef extends BlockDef<WallBlock> {
    @DataGenOnly
    private ResourceLocation locSide, locSideTall, locPost;
    private Supplier<ResourceLocation> textureSide;

    public WallBlockDef(String id, String name, BlockDef<?> baseBlock) {
        super(id, name, new WallBlock(getPeekProp()));
        if (Mod.isEnvDataGen) {
            this.locSide = loc.withSuffix("_side");
            this.locSideTall = loc.withSuffix("_side_tall");
            this.locPost = loc.withSuffix("_post");
            this.textureSide = () -> getTexture(baseBlock, "side", "all");
        }
    }

    @Override
    @DataGenOnly
    public void provideData() {
        super.provideData();
        DataProviders.BLOCK_TAGS.of(BlockTags.WALLS).add(block).replaceVanilla(false);
    }

    @Override
    @DataGenOnly
    protected void provideBlockState() {
        MultiPartBlockStateBuilder builder = DataProviders.BLOCK_STATE.multipart(loc);
        String[] directions = {"north", "east", "south", "west"};  //north方向上side模型无需旋转
        for (int i = 0; i < 4; i++) {
            builder.apply(new Model(locSide).rotationY(i).uvLock(i != 0))
                    .when(directions[i], "low");
            builder.apply(new Model(locSideTall).rotationY(i).uvLock(i != 0))
                    .when(directions[i], "tall");
        }
        builder.apply(new Model(locPost))
                .when("up", "true");
    }

    @Override
    @DataGenOnly
    protected void provideModel() {
        //不需要为自己创建模型文件
        ResourceLocation side = textureSide.get();
        DataProviders.MODEL_BLOCK.model(locSide)
                .parent("block/template_wall_side").texture("wall", side);
        DataProviders.MODEL_BLOCK.model(locSideTall)
                .parent("block/template_wall_side_tall").texture("wall", side);
        DataProviders.MODEL_BLOCK.model(locPost)
                .parent("block/template_wall_post").texture("wall", side);
        DataProviders.MODEL_ITEM.model(loc)
                .parent("block/wall_inventory").texture("wall", side);
    }
}
