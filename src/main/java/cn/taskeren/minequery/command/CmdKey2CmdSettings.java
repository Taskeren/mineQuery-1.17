package cn.taskeren.minequery.command;

import cn.taskeren.minequery.gui.Key2CommandSettingScreen;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CmdKey2CmdSettings {

	public static LiteralArgumentBuilder<FabricClientCommandSource> getBuilder() {
		return LiteralArgumentBuilder.<FabricClientCommandSource>literal("key2cmd").executes(ctx -> {
			// according to https://www.reddit.com/r/fabricmc/comments/w165i1/changing_the_clients_screen_from_a_command_help/
			// any screen will be closed after command execution, so I need to open the screen after the end of the command.
			ctx.getSource().getClient().send(Key2CommandSettingScreen.INSTANCE::show);
			return Command.SINGLE_SUCCESS;
		});
	}

}
