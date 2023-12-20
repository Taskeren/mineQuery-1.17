package cn.taskeren.minequery.command

import cn.taskeren.brigadierx.executesX
import cn.taskeren.brigadierx.newLiteralArgBuilder
import cn.taskeren.minequery.gui.Key2CommandSettingScreen
import com.mojang.brigadier.Command
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

internal val CommandKey2CommandSettings = newLiteralArgBuilder<FabricClientCommandSource>("key2cmd") {
	executesX {
		// according to https://www.reddit.com/r/fabricmc/comments/w165i1/changing_the_clients_screen_from_a_command_help/
		// any screen will be closed after command execution, so I need to open the screen after the end of the command.
		it.source.client.send {
			Key2CommandSettingScreen.show()
		}
		Command.SINGLE_SUCCESS
	}
}