package cn.taskeren.minequery.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static cn.taskeren.minequery.MineQueryMod.config;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin {

	@Shadow
	protected abstract void onConfirmQuit(boolean quit);

	@Inject(at = @At("RETURN"), method = "init()V")
	private void init(CallbackInfo info) {
		if(config().autoRevive) {
			ClientPlayerEntity cp = MinecraftClient.getInstance().player;
			if(cp != null) {
				onConfirmQuit(false);
				cp.sendMessage(new TranslatableText("text.minequery.autorevive"), true);
			}
		}
	}

}
