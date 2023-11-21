package stupidcoder.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import stupidcoder.common.core.Mod;
import stupidcoder.common.datagen.generators.ItemModelGenerator;

public class BaseItem extends Item {
    public final ResourceLocation location;

    public BaseItem(FabricItemSettings properties, ResourceLocation location) {
        super(properties);
        this.location = location;
        register();
    }

    public BaseItem(FabricItemSettings properties, String id) {
        this(properties, new ResourceLocation(Mod.MOD_ID, id));
    }

    public BaseItem(String id) {
        this(new FabricItemSettings(), id);
    }

    /**
     * 注册物品的模型；默认逻辑为生成物品的模型文件，在fabric的runDataGen任务中调用
     */
    protected void registerModel() {
        if (Mod.isEnvDataGen) {
            ItemModelGenerator.getInstance()
                    .model(location)
                    .parent("minecraft:item/generated")
                    .texture("layer0", location);
        }
    }

    /**
     * 在所有环境下的注册工作
     */
    protected void register() {
        if (!Mod.isEnvDataGen) {
            Registry.register(BuiltInRegistries.ITEM, location, this);
        }
        registerModel();
    }
}
