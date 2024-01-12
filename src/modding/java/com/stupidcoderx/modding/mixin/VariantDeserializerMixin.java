package com.stupidcoderx.modding.mixin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stupidcoderx.modding.util.BlockStateDefinitionHook;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(Variant.Deserializer.class)
public class VariantDeserializerMixin {

    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/Variant;", at = @At("RETURN"), cancellable = true)
    public void addAdditionalRotationOptions(JsonElement json, Type type, JsonDeserializationContext context,
                                             CallbackInfoReturnable<Variant> cri) {
        Variant v = cri.getReturnValue();
        JsonObject obj = json.getAsJsonObject();
        if (obj.has("z")) {
            var xRot = GsonHelper.getAsInt(obj, "x", 0);
            var yRot = GsonHelper.getAsInt(obj, "y", 0);
            var zRot = GsonHelper.getAsInt(obj, "z", 0);
            cri.setReturnValue(BlockStateDefinitionHook.rotateVariant(v, xRot, yRot, zRot));
        }
    }
}
