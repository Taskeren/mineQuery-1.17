package cn.taskeren.minequery.features

import cn.taskeren.minequery.MQKeyRegistry
import cn.taskeren.minequery.MineQuery
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN

object KeyToCommand {

	internal const val KEY_BINDING_SIZE = 10
	private val keyBindings = mutableListOf<KeyBinding>()

	fun init() {
		repeat(KEY_BINDING_SIZE) {
			val kb = createKeyBinding(it)
			keyBindings.add(kb)
		}

		ClientTickEvents.END_CLIENT_TICK.register {
			val cp = it.player ?: return@register
			keyBindings.forEachIndexed { i, kb ->
				if(kb.wasPressed()) {
					val cmd = getConfiguredCommand(i)
					if(cmd.isNotBlank()) {
						if(cmd.startsWith("/")) {
							cp.networkHandler.sendChatCommand(cmd.substring(1))
						} else {
							cp.networkHandler.sendChatMessage(cmd)
						}
						cp.sendMessage(Text.translatable("text.minequery.key2cmd.success"), true)
					} else {
						cp.sendMessage(Text.translatable("text.minequery.key2cmd.emptyOrUnconfigured"), false)
					}
				}
			}
		}
	}

	private fun createKeyBinding(i: Int): KeyBinding {
		return MQKeyRegistry.registerKey("key2cmd.$i", GLFW_KEY_UNKNOWN)
	}

	private fun getConfiguredCommand(i: Int): String {
		return getCommandList().getOrElse(i) { "" }
	}

	private fun getCommandList(): MutableList<String> {
		return MineQuery.config.key2Cmd
	}

}