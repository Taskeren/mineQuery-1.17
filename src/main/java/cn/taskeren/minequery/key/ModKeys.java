package cn.taskeren.minequery.key;

import cn.taskeren.minequery.MineQueryMod;
import cn.taskeren.minequery.config.MineQueryConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;

public class ModKeys {

	public static final String KEY_CATEGORY = "keybinding.minequery";

	public static KeyBinding keyFeedEm;
	public static KeyBinding openModConfig;

	public static void init() {
		keyFeedEm = registerKey("feed_em", GLFW_KEY_Y);
		openModConfig = registerKey("open_config", GLFW_KEY_M, (cli) -> {
			var screen = AutoConfig.getConfigScreen(MineQueryConfig.class, cli.currentScreen).get();
			cli.setScreen(screen);
		});

		// register general tick listener
		ClientTickEvents.END_CLIENT_TICK.register(ModKeys::tick);
	}

	static KeyBinding registerKey(String keyId, int keyCode) {
		return KeyBindingHelper
				.registerKeyBinding(new KeyBinding("keybinding.minequery."+keyId, keyCode, KEY_CATEGORY));
	}

	private static final List<Consumer<MinecraftClient>> tickList = Lists.newArrayList();

	public static KeyBinding registerKey(String keyId, int keyCode, Consumer<MinecraftClient> onPressed) {
		var kb = registerKey(keyId, keyCode);
		tickList.add(client -> {
			if(kb.isPressed()) {
				onPressed.accept(client);
			}
		});
		return kb;
	}

	static void tick(MinecraftClient client) {
		tickList.forEach(toTick -> {
			try {
				toTick.accept(client);
			} catch (Exception ex) {
				MineQueryMod.LOGGER.warn("Exception occurred when ticking keys.", ex);
			}
		});
	}

}
