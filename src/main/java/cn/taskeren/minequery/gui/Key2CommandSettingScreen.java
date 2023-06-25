package cn.taskeren.minequery.gui;

import cn.taskeren.minequery.gui.clothconfig.MineQueryStringListListEntry;
import cn.taskeren.minequery.key.KeyToCommand;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

import static cn.taskeren.minequery.MineQueryMod.config;
import static cn.taskeren.minequery.MineQueryMod.saveConfig;

public class Key2CommandSettingScreen {

	public static void show() {
		var screen = getScreen(MinecraftClient.getInstance().currentScreen);
		MinecraftClient.getInstance().setScreen(screen);
	}

	private static Screen getScreen(Screen parent) {
		var b = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(Text.translatable("text.autoconfig.minequery.option.key2Cmd"));

		// because it is a single category config, so the label will not render.
		// leave it alone.
		var c = b.getOrCreateCategory(Text.of("Nothing"));

		var entry = new MineQueryStringListListEntry(
				Text.translatable("text.autoconfig.minequery.option.key2Cmd.short"),
				getValues(),
				true,
				Optional::empty,
				Key2CommandSettingScreen::updateValues,
				Key2CommandSettingScreen::getValues,
				Text.of("R"),
				false,
				true,
				false,
				MineQueryStringListListEntry.MineQueryStringListCell::new
		);
		entry.maxCount = KeyToCommand.KEY_BINDING_SIZE;

		c.addEntry(entry);

		return b.build();
	}

	private static List<String> getValues() {
		return config().key2Cmd;
	}

	private static void updateValues(List<String> newValue) {
		var list = config().key2Cmd;
		list.clear();
		list.addAll(newValue);
		saveConfig();
	}

}
