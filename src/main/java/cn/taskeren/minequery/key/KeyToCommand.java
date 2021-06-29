package cn.taskeren.minequery.key;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

import static cn.taskeren.minequery.MineQueryMod.config;

public class KeyToCommand {

	public static final int KEY_BINDING_SIZE = 10;
	public static final int KEY_CODE_UNKNOWN = -1;
	public static final String KEY_CATEGORY = "keybinding.minequery";

	public static ArrayList<KeyBinding> keyBindings = new ArrayList<>();

	private KeyToCommand() {}

	public static int getKeyBindingSize() {
		return KEY_BINDING_SIZE;
	}

	public static void init() {
		init(getKeyBindingSize());
		ClientTickEvents.END_CLIENT_TICK.register(KeyToCommand::tick);
	}

	public static void init(int total) {
		for(int i = 0; i < total; i++) {
			int k = keyBindings.size();
			KeyBinding binding = createKeyBinding(k);
			keyBindings.add(binding);
		}
	}

	private static KeyBinding createKeyBinding(int i) {
		KeyBinding binding = new KeyBinding(getKeyTranslation(i), KEY_CODE_UNKNOWN, KEY_CATEGORY);
		KeyBindingHelper.registerKeyBinding(binding);
		return binding;
	}

	static void tick(MinecraftClient client) {
		for(int i = 0; i < keyBindings.size(); i++) {
			if(keyBindings.get(i).wasPressed()) {
				ClientPlayerEntity cp = client.player;
				if(cp != null) {
					String cmd = getConfiguredCommand(i);
					if(!cmd.isBlank()) {
						cp.sendChatMessage(cmd);
					} else {
						cp.sendMessage(new TranslatableText("text.minequery.key2cmd.emptyOrUnconfigured"), false);
					}
				}
			}
		}
	}

	private static String getKeyTranslation(int key) {
		return "keybinding.minequery.key2cmd."+key;
	}

	private static String getConfiguredCommand(int i) {
		return i >= 0 && i < getReversedCommandList().size() ? getReversedCommandList().get(i) : "";
	}

	private static List<String> getReversedCommandList() {
		return Lists.reverse(config().key2Cmd);
	}

}
