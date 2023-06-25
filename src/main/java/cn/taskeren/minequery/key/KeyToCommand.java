package cn.taskeren.minequery.key;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static cn.taskeren.minequery.MineQueryMod.config;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;

public class KeyToCommand {

	public static final int KEY_BINDING_SIZE = 10;

	public static ArrayList<KeyBinding> keyBindings = new ArrayList<>();

	private KeyToCommand() {}

	public static void init() {
		for(int i = 0; i < KEY_BINDING_SIZE; i++) {
			int k = keyBindings.size();
			KeyBinding binding = createKeyBinding(k);
			keyBindings.add(binding);
		}

		ClientTickEvents.END_CLIENT_TICK.register(KeyToCommand::tick);
	}

	private static KeyBinding createKeyBinding(int i) {
		return ModKeys.registerKey(getKeyTranslation(i), GLFW_KEY_UNKNOWN);
	}

	static void tick(MinecraftClient client) {
		for(int i = 0; i < keyBindings.size(); i++) {
			if(keyBindings.get(i).wasPressed()) {
				ClientPlayerEntity cp = client.player;
				if(cp != null) {
					String cmd = getConfiguredCommand(i);
					if(!cmd.isBlank()) {
						if(cmd.startsWith("/")) {
							cp.networkHandler.sendChatCommand(cmd.substring(1));
						} else {
							cp.networkHandler.sendChatMessage(cmd);
						}
						cp.sendMessage(Text.translatable("text.minequery.key2cmd.success"), true);
					} else {
						cp.sendMessage(Text.translatable("text.minequery.key2cmd.emptyOrUnconfigured"), false);
					}
				}
			}
		}
	}

	private static String getKeyTranslation(int key) {
		return "key2cmd."+key;
	}

	private static String getConfiguredCommand(int i) {
		return i >= 0 && i < getCommandList().size() ? getCommandList().get(i) : "";
	}

	private static List<String> getCommandList() {
		return config().key2Cmd;
	}

}
