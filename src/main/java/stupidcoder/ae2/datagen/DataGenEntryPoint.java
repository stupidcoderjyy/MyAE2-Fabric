package stupidcoder.ae2.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import stupidcoder.ae2.core.registry.AEItems;
import stupidcoder.common.datagen.generators.ItemModelGenerator;
import stupidcoder.common.datagen.model.ResourceFileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
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
        pack.addProvider(out -> new ItemModelGenerator(out, helper, AEItems::register));
    }
}
