package cn.taskeren.minequery.mixin;

import cn.taskeren.minequery.MineQuery;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin {

	@Inject(at = @At("RETURN"), method = "init()V")
	private void init(CallbackInfo info) {
		if(MineQuery.INSTANCE.getConfig().getAutoRevive()) {
			ClientPlayerEntity cp = MinecraftClient.getInstance().player;
			if(cp != null) {
				cp.requestRespawn();
				cp.sendMessage(Text.translatable("text.minequery.autorevive"), true);
			}
		}
	}

}
