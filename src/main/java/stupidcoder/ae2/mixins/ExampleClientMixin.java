package stupidcoder.ae2.mixins;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@SuppressWarnings("unused")
public class ExampleClientMixin {
	@Inject(at = @At("HEAD"), method = "run()V")
	private void run(CallbackInfo info) {
		// This code is injected into the start of MinecraftClient.run()V
        LogUtils.getLogger().info("run");
	}
}