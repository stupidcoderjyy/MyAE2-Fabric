package com.stupidcoderx.ae2.elements.blocks.glass;

import com.stupidcoderx.modding.client.BuiltInModelRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.Model;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import com.stupidcoderx.modding.element.block.BlockDef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class QuartzGlassBlockDef extends BlockDef<QuartzGlassBlock> {
    private static final String ID = "quartz_glass";
    private static final String BLOCK_ID = "block/quartz_glass";
    static final ResourceLocation TEXTURE_BLOCK_LOC = Mod.modLoc("block/glass/" + ID);

    private QuartzGlassBlockDef(String id, String name, QuartzGlassBlock block) {
        super(id, name, block);
    }

    public static QuartzGlassBlockDef create(String name, boolean isVibrant) {
        QuartzGlassBlockDef def;
        if (isVibrant) {
            inheritPropDisposable(p -> p
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .lightLevel(b -> 15));
            def = new QuartzGlassBlockDef(ID + "_vibrant", name, new QuartzGlassBlock(getPeekProp()));
        } else {
            inheritPropDisposable(p -> p
                    .noOcclusion()
                    .isValidSpawn(Blocks::never));
            def = new QuartzGlassBlockDef(ID, name, new QuartzGlassBlock(getPeekProp()));
        }
        return def;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientRegister() {
        BuiltInModelRegistry.INSTANCE.register(BLOCK_ID, new QuartzGlassModel());
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.translucent());
    }

    @Override
    protected ModelBuilder provideBlockModel() {
        return DataProviders.MODEL_BLOCK.model(loc).loader(BLOCK_ID);
    }

    @Override
    protected void provideItemModel() {
        DataProviders.MODEL_ITEM.model(loc)
                .parent("block/cube_all")
                .texture("all", Mod.modLoc(ID));
    }

    @Override
    protected void provideBlockState() {
        DataProviders.BLOCK_STATE.variants(loc)
                .condition(state -> new Model(ID));
    }
}
