package com.stupidcoderx.ae2.client.tesr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.stupidcoderx.ae2.elements.blockentities.SkyChestTE;
import com.stupidcoderx.ae2.registry.AEBlockEntities;
import com.stupidcoderx.modding.client.render.IModTESR;
import com.stupidcoderx.modding.core.Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;

@Environment(EnvType.CLIENT)
public class SkyChestTESR implements IModTESR<SkyChestTE> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            Mod.modLoc("sky_chest"), "main");
    private static final Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS,
            Mod.modLoc("block/sky_chest"));
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    public SkyChestTESR(BlockEntityRendererProvider.Context context) {
        ModelPart mp = context.bakeLayer(MODEL_LAYER);
        this.lid = mp.getChild("lid");
        this.bottom = mp.getChild("bottom");
        this.lock = mp.getChild("lock");
    }

    @Override
    public void render(SkyChestTE blockEntity, float f, PoseStack poseStack, MultiBufferSource bufferIn, int i, int j) {
        poseStack.pushPose();
        VertexConsumer vc = TEXTURE.buffer(bufferIn, RenderType::entityCutout);
        lid.render(poseStack, vc, i, j);
        bottom.render(poseStack, vc, i, j);
        lock.render(poseStack, vc, i, j);
        poseStack.popPose();
    }

    public static void register() {
        AEBlockEntities.SKY_CHEST.registerRenderer(SkyChestTESR::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_LAYER, SkyChestTESR::createLayer);
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        CubeListBuilder bottom = CubeListBuilder.create()
                .texOffs(0, 19)
                .addBox(1, 0, 1, 14, 10, 14);
        CubeListBuilder lid = CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0,0,0,14,5,14);
        CubeListBuilder lock = CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(7,-1,15,2,4,1);
        part.addOrReplaceChild("bottom", bottom, PartPose.ZERO);
        part.addOrReplaceChild("lid", lid, PartPose.offset(1,10,1));
        part.addOrReplaceChild("lock", lock, PartPose.offset(0,9,0));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
