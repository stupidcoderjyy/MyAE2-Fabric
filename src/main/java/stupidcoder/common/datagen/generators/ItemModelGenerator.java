package stupidcoder.common.datagen.generators;

import net.minecraft.data.PackOutput;
import stupidcoder.common.datagen.model.IGeneratorDataRegistry;
import stupidcoder.common.datagen.model.ModelBuilder;
import stupidcoder.common.datagen.model.ModelGenerator;
import stupidcoder.common.datagen.model.ResourceFileHelper;

import java.util.Objects;

public class ItemModelGenerator extends ModelGenerator<ItemModelGenerator> {
    protected static ItemModelGenerator instance;

    public static ItemModelGenerator getInstance() {
        Objects.requireNonNull(instance, "ItemModelGenerator not initialized");
        return instance;
    }

    public ItemModelGenerator(PackOutput output, ResourceFileHelper helper, IGeneratorDataRegistry registry) {
        super(output, "item", loc -> new ModelBuilder(loc, helper), helper, registry);
        instance = this;
    }

    @Override
    public String getName() {
        return "ItemModel";
    }
}
