package com.stupidcoderx.common.datagen.generators;

import com.stupidcoderx.common.datagen.model.IGeneratorDataRegistry;
import com.stupidcoderx.common.datagen.model.ModelBuilder;
import com.stupidcoderx.common.datagen.model.ModelGenerator;
import com.stupidcoderx.common.datagen.model.ResourceFileHelper;
import net.minecraft.data.PackOutput;

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
