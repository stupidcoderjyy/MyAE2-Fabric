package com.stupidcoderx.modding.element;

import com.google.common.base.Preconditions;
import com.stupidcoderx.modding.core.IRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.datagen.DataProviders;
import com.stupidcoderx.modding.datagen.blockstate.ModelList;
import com.stupidcoderx.modding.datagen.model.ModelBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class BaseBlock extends Block implements IRegistry {
    public final ResourceLocation registryLoc;
    protected BlockItem item;
    private Function<BaseBlock, BlockItem> itemBuilder = b ->
            new BlockItem(this, new Item.Properties());

    public BaseBlock(Properties properties, String id) {
        super(properties);
        this.registryLoc = Mod.modLoc(id);
        Mod.BLOCK_REGISTRY.add(this);
    }

    public BaseBlock blockItem(Function<BaseBlock, BlockItem> itemBuilder) {
        this.itemBuilder = itemBuilder;
        return this;
    }

    public BaseBlock creativeTab(ModCreativeTab tab) {
        tab.add(this);
        return this;
    }

    public BaseBlock creativeTab(ResourceKey<CreativeModeTab> tabKey) {
        ItemGroupEvents.modifyEntriesEvent(tabKey).register(entries -> entries.accept(this));
        return this;
    }

    public ItemStack stack(int size) {
        return new ItemStack(this, size);
    }

    public BlockItem getItem() {
        return item;
    }

    @Override
    public void commonRegister() {
        this.item = itemBuilder.apply(this);
        Preconditions.checkState(registryLoc != null, "missing id");
        Preconditions.checkState(item != null, "null blockItem");
        Registry.register(BuiltInRegistries.BLOCK, registryLoc, this);
        Registry.register(BuiltInRegistries.ITEM, registryLoc, item);
    }

    @Override
    public void provideData() {
        Preconditions.checkState(registryLoc != null, "missing id");
        ModelBuilder mb = DataProviders.MODEL_BLOCK.getOrCreateModel(registryLoc);
        generateBlockModel(mb);
        generateItemModel(mb);
        generateBlockState();
    }

    protected void generateBlockModel(ModelBuilder mbBlock) {
        mbBlock.parent("minecraft:block/cube_all")
                .texture("all", registryLoc);
    }

    protected void generateItemModel(ModelBuilder mbBlock) {
        DataProviders.MODEL_ITEM.getOrCreateModel(registryLoc)
            .parent(mbBlock);
    }

    protected void generateBlockState() {
        DataProviders.BLOCK_STATE.variants(registryLoc)
                .condition(state -> new ModelList().create(registryLoc));
    }
}
