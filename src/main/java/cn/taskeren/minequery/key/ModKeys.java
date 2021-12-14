package cn.taskeren.minequery.key;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;

public class ModKeys {

	public static final String KEY_CATEGORY = "keybinding.minequery";

	public static KeyBinding keyFeedEm;

	public static void init() {
		keyFeedEm = registerKey("feed_em", GLFW_KEY_Y);
	}

	static KeyBinding registerKey(String keyId, int keyCode) {
		return KeyBindingHelper
				.registerKeyBinding(new KeyBinding("keybinding.minequery."+keyId, keyCode, KEY_CATEGORY));
	}

}
