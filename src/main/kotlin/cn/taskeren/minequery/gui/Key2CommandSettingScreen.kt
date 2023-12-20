package cn.taskeren.minequery.gui

import cn.taskeren.minequery.MineQuery
import cn.taskeren.minequery.features.KeyToCommand
import cn.taskeren.minequery.gui.clothconfig.MineQueryStringListListEntry
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.util.*

object Key2CommandSettingScreen {

	fun show() {
		val screen = getScreen(MinecraftClient.getInstance().currentScreen)
		MinecraftClient.getInstance().setScreen(screen)
	}

	fun getScreen(parent: Screen?): Screen {
		val b = ConfigBuilder.create()
			.setParentScreen(parent)
			.setTitle(Text.translatable("text.autoconfig.minequery.option.key2Cmd"))

		val c = b.getOrCreateCategory(Text.of("Nothing"))

		val entry = MineQueryStringListListEntry(
			Text.translatable("text.autoconfig.minequery.option.key2Cmd.short"),
			getValues(),
			true,
			{ Optional.empty() },
			{ updateValues(it) },
			{ getValues() },
			Text.of("R"),
			false,
			true,
			false,
			MineQueryStringListListEntry::MineQueryStringListCell
		)
		entry.maxCount = KeyToCommand.KEY_BINDING_SIZE

		c.addEntry(entry)

		return b.build()

	}

	private fun getValues(): MutableList<String> {
		return MineQuery.config.key2Cmd
	}

	private fun updateValues(values: List<String?>) {
		MineQuery.config.key2Cmd.clear()
		MineQuery.config.key2Cmd.addAll(values.filterNotNull())
		MineQuery.saveConfig()
	}

}