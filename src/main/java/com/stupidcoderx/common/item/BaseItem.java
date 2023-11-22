package com.stupidcoderx.common.item;

import com.stupidcoderx.common.core.IRegistry;
import com.stupidcoderx.common.core.Mod;
import com.stupidcoderx.common.datagen.generators.ItemModelGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class BaseItem extends Item implements IRegistry {
    public final ResourceLocation location;

    public BaseItem(FabricItemSettings properties, ResourceLocation location) {
        super(properties);
        this.location = location;
        Mod.ITEM_REGISTRY.add(this);
    }

    public BaseItem(FabricItemSettings properties, String id) {
        this(properties, new ResourceLocation(Mod.getModId(), id));
    }

    public BaseItem(String id) {
        this(new FabricItemSettings(), id);
    }

    public BaseItem(ResourceLocation location) {
        this(new FabricItemSettings(), location);
    }

    @Override
    public void commonRegister() {
        Registry.register(BuiltInRegistries.ITEM, location, this);
    }

    @Override
    public void buildData() {
        ItemModelGenerator.getInstance()
                .model(location)
                .parent("minecraft:item/generated")
                .texture("layer0", location);
    }
}
