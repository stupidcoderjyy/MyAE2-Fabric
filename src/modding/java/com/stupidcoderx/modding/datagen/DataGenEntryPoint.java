package com.stupidcoderx.modding.datagen;

import com.stupidcoderx.modding.core.Mod;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * 模组数据生成进入点，有需要可继承此类并修改fabric.mod.json中的进入点
 */
public class DataGenEntryPoint implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        DataProviders.init();
        provideData();
        ModDataProvider.register(generator.createPack());
    }

    /**
     * 数据构造
     */
    protected void provideData() {
        Mod.ITEM_LIKE_REGISTRY.provideData();
        Mod.RECIPE_REGISTRY.provideData();
        Mod.CREATIVE_TAB_REGISTRY.provideData();
    }
}
