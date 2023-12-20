package cn.taskeren.minequery

import cn.taskeren.minequery.config.MineQueryConfig
import cn.taskeren.minequery.gui.Key2CommandSettingScreen
import me.shedaniel.autoconfig.AutoConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil.*

object MQKeyRegistry {

	private const val KEY_CATEGORY = "keybinding.minequery"

	private val tickList = mutableListOf<(MinecraftClient) -> Unit>()

	val keyFeedEm: KeyBinding = registerKey("feed_em", GLFW_KEY_Y)
	val keyOpenModConfig: KeyBinding = registerKey("open_config", GLFW_KEY_M) {
		val screen = AutoConfig.getConfigScreen(MineQueryConfig::class.java, it.currentScreen).get()
		it.setScreen(screen)
	}
	val keyKey2CmdSettings = registerKey("key_to_cmd_settings", GLFW_KEY_J) {
		Key2CommandSettingScreen.show()
	}

	internal fun registerKey(keyId: String, keyCode: Int, onPressed: ((MinecraftClient) -> Unit)? = null): KeyBinding {
		val kb = KeyBindingHelper.registerKeyBinding(KeyBinding("$KEY_CATEGORY.$keyId", keyCode, KEY_CATEGORY))
		if(onPressed != null) {
			tickList += {
				if(kb.isPressed) {
					onPressed(it)
				}
			}
		}
		return kb
	}

	fun init() {
		ClientTickEvents.END_CLIENT_TICK.register { client ->
			tickList.forEach {
				runCatching {
					it(client)
				}.onFailure {
					MineQuery.logger.warn("Exception occurred when ticking keys!", it)
				}
			}
		}
	}

}