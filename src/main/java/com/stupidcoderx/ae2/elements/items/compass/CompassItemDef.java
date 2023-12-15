package com.stupidcoderx.ae2.elements.items.compass;

import com.stupidcoderx.modding.core.BuiltInModelRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.model.ModelTransform;
import com.stupidcoderx.modding.datagen.model.elements.Direction;
import com.stupidcoderx.modding.datagen.model.elements.IBasePointStrategy;
import com.stupidcoderx.modding.datagen.model.elements.ICubeCreateStrategy;
import com.stupidcoderx.modding.datagen.model.elements.Structure;
import com.stupidcoderx.modding.element.ModCreativeTab;
import com.stupidcoderx.modding.element.item.ItemDef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class CompassItemDef extends ItemDef<CompassItemDef, Item> {
    private static final String ID = "meteorite_compass";
    static final ResourceLocation BASE = Mod.modLoc("item/" + ID + "_base");
    static final ResourceLocation POINTER = Mod.modLoc("item/" + ID + "_pointer");

    public CompassItemDef(ModCreativeTab tab) {
        super(ID, new Item(new Item.Properties().stacksTo(1)));
        creativeTab(tab);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientRegister() {
        BuiltInModelRegistry.INSTANCE.register(Mod.modLoc("item/" + ID), new CompassUnbakedModel());
    }

    @Override
    protected void provideModel() {
        Structure base = new Structure(IBasePointStrategy.NY)
                .cubeCreateStrategy(ICubeCreateStrategy.NY)
                .create(6,2,6)  //指南针外壳
                .process(c -> c.faceAll().uv(0,0).texture("#0"))
                .scoop(cfg -> cfg
                        .range(4,1,4)
                        .shift(1, Direction.PY_UP))
                .create(1,1,1)  //指南针轴
                .process(c -> c.faceAll().uv(0,0).texture("#0"))
                .shift(1, Direction.PY_UP);
        Structure pointer = new Structure(IBasePointStrategy.NY)
                .cubeCreateStrategy(ICubeCreateStrategy.PZ)
                .create(0.5f, 0.5f, 2.0f)
                .shift(1.25f, Direction.PY_UP)
                .process(c -> c.faceAll().uv(0,8).texture("#0"));
        DataProviders.MODEL_ITEM.getOrCreateModel(BASE)
                .parent("block/block")
                .texture("0", loc)
                .struct(base)
                .displayTransform(ModelTransform.rightHandFirstPerson()
                        .rotation(10,0,0).translation(0, 8, -12).scale(2))
                .displayTransform(ModelTransform.gui()
                        .rotation(30, 45, 0).translation(0, 8, 0).scale(1.5f));
        DataProviders.MODEL_ITEM.getOrCreateModel(POINTER)
                .parent("block/block")
                .texture("0", loc)
                .struct(pointer);
    }
}
