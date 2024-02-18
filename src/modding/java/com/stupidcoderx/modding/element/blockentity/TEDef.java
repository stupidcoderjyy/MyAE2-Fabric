package com.stupidcoderx.modding.element.blockentity;

import com.stupidcoderx.modding.core.ICommonRegistry;
import com.stupidcoderx.modding.core.Mod;
import com.stupidcoderx.modding.element.block.EntityBlockDef;
import com.stupidcoderx.modding.element.block.ModEntityBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class TEDef<E extends BlockEntity> implements ICommonRegistry {
    public final BlockEntityType<E> type;
    protected final ResourceLocation loc;
    private final Block[] blockArr;

    public TEDef(String shortId,
                 IBlockEntityFactory<E> beFactory,
                 EntityBlockDef<?>... blocks) {
        this.blockArr = Arrays.stream(blocks).map(d -> d.block).toArray(Block[]::new);
        this.loc = Mod.modLoc(shortId);
        var ref = new AtomicReference<BlockEntityType<E>>();
        this.type = Builder.of((p, s) -> beFactory.create(ref.get(), p, s), blockArr).build(null);
        ref.set(type);
        Mod.addCommonRegistry(this);
    }

    @Override
    public void commonRegister() {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, loc, type);
        for (Block b : blockArr) {
            ((ModEntityBlock<E>) b).setType(type);
        }
    }

    @Environment(EnvType.CLIENT)
    public void registerRenderer(BlockEntityRendererProvider<E> provider) {
        BlockEntityRendererRegistry.register(type, provider);
    }
}
