package com.stupidcoderx.modding.datagen;

import com.stupidcoderx.modding.core.IDataGenRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * 模组数据生成进入点，有需要可继承此类并修改fabric.mod.json中的进入点
 */
public class DataGenEntryPoint implements DataGeneratorEntrypoint {
    public static final List<IDataGenRegistry> DATA_GEN_REGISTRIES = new ArrayList<>();

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        DataProviders.init();
        DATA_GEN_REGISTRIES.forEach(IDataGenRegistry::generateData);
        ModDataProvider.register(generator.createPack());
    }
}
