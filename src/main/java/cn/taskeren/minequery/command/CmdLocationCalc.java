package cn.taskeren.minequery.command;

import cn.taskeren.minequery.command.client_pos.ClientPosArgumentType;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColumnPos;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CmdLocationCalc {

	public static LiteralArgumentBuilder<FabricClientCommandSource> getBuilder() {
		return literal("localc")
				.then(literal("toNether")
						.then(argument("location", ClientPosArgumentType.columnPos()).executes(CmdLocationCalc::calcToNether)))
				.then(literal("toOverworld")
						.then(argument("location", ClientPosArgumentType.columnPos()).executes(CmdLocationCalc::calcToOverworld)));
	}

	static int calcToNether(CommandContext<FabricClientCommandSource> ctx) {
		ColumnPos pos = ClientPosArgumentType.getColumnPos(ctx, "location");
		double x = Math.floor((float)pos.x()/8);
		double z = Math.floor((float)pos.z()/8);
		ctx.getSource().sendFeedback(Text.translatable("text.minequery.localc.to_nether", x, z));
		return Command.SINGLE_SUCCESS;
	}

	static int calcToOverworld(CommandContext<FabricClientCommandSource> ctx) {
		ColumnPos pos = ClientPosArgumentType.getColumnPos(ctx, "location");
		double x = Math.floor((float)pos.x()*8);
		double z = Math.floor((float)pos.z()*8);
		ctx.getSource().sendFeedback(Text.translatable("text.minequery.localc.to_overworld", x, z));
		return Command.SINGLE_SUCCESS;
	}

}
