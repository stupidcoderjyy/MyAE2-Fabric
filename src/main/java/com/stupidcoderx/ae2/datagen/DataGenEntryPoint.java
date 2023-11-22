package com.stupidcoderx.ae2.datagen;

import com.stupidcoderx.common.core.Mod;
import com.stupidcoderx.common.datagen.generators.ItemModelGenerator;
import com.stupidcoderx.common.datagen.model.ResourceFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DataGenEntryPoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        String[] existingResources = System.getProperty("mod.dataGen.existingResourcesPaths").split(";");
        List<Path> paths = Arrays.stream(existingResources).map(Paths::get).toList();
        //创建资源管理器，资源管理器能够管理原版资源、手动注册的资源和外部资源（即paths对应的资源）
        ResourceFileHelper helper = new ResourceFileHelper(paths);
        DataGenerator.PackGenerator pack = generator.createPack();
        register(pack, helper);
    }

    private void register(DataGenerator.PackGenerator pack, ResourceFileHelper helper) {
        pack.addProvider(out -> new ItemModelGenerator(out, helper, Mod.ITEM_REGISTRY::buildData));
    }
}
