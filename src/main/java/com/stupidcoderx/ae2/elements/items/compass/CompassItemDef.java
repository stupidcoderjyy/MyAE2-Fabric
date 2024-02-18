package com.stupidcoderx.ae2.elements.items.compass;

import com.stupidcoderx.modding.client.BuiltInModelRegistry;
import com.stupidcoderx.modding.core.IClientRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.model.Display;
import com.stupidcoderx.modding.datagen.model.elements.Direction;
import com.stupidcoderx.modding.datagen.model.elements.IBasePointStrategy;
import com.stupidcoderx.modding.datagen.model.elements.ICubeCreateStrategy;
import com.stupidcoderx.modding.datagen.model.elements.Structure;
import com.stupidcoderx.modding.element.item.ItemDef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class CompassItemDef extends ItemDef<Item> implements IClientRegistry {
    private static final String ID = "meteorite_compass";
    private static final String EXPANDED_ID = "item/" + ID;
    static final ResourceLocation BASE_LOC = Mod.modLoc(EXPANDED_ID + "_base");
    static final ResourceLocation POINTER_LOC = Mod.modLoc(EXPANDED_ID + "_pointer");

    public CompassItemDef(String name) {
        super(ID, name, new Item(new Item.Properties().stacksTo(1)));
        Mod.addClientRegistry(this);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientRegister() {
        BuiltInModelRegistry.INSTANCE.register(EXPANDED_ID, new CompassModel());
    }

    @Override
    protected void provideModel() {
        Structure base = new Structure(IBasePointStrategy.NY)
                .cubeCreateStrategy(ICubeCreateStrategy.NY)
                .create(6,2,6)  //指南针外壳
                .process(c -> c.faceAll().uv(0,0).texture("#0"))
                .scoop(cfg -> cfg
                        .range(4,1,4)
                        .shift(1, Direction.PY))
                .create(1,1,1)  //指南针轴
                .process(c -> c.faceAll().uv(0,0).texture("#0"))
                .shift(1, Direction.PY);
        Structure pointer = new Structure(IBasePointStrategy.NY)
                .cubeCreateStrategy(ICubeCreateStrategy.NZ)
                .create(0.5f, 0.5f, 2.0f)
                .shift(1.25f, Direction.PY)
                .process(c -> c
                        .face(Direction.xy()).forceUv(0,8,16,3).texture("#0")
                        .face(Direction.z()).uv(0,8).texture("#0").rotate(1)
                        .face(Direction.PY).rotate(3)
                        .face(Direction.NX).rotate(2)
                        .face(Direction.NY, Direction.NZ).removeFaces());
        DataProviders.MODEL_ITEM.model(BASE_LOC)
                .parent("block/block")
                .texture("0", loc)
                .struct(base)
                .display(Display.rightHandFirstPerson()
                        .rotation(10,0,0).translation(0, 8, -12).scale(2))
                .display(Display.leftHandFirstPerson()
                        .rotation(10,0,0).translation(0, 8, -12).scale(2))
                .display(Display.gui()
                        .rotation(30, 45, 0).translation(0, 8, 0).scale(1.5f))
                .display(Display.rightHandThirdPerson()
                        .rotation(75,45,0).translation(0, 2.5f, 2).scale(0.375f))
                .display(Display.fixed()
                        .rotation(270,0,0).translation(0, 0, -10).scale(1.5f));
        DataProviders.MODEL_ITEM.model(POINTER_LOC)
                .parent("block/block")
                .texture("0", loc)
                .struct(pointer);
    }
}
