package cn.taskeren.minequery.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.endreman0.calculator.Calculator;
import io.github.endreman0.calculator.expression.type.Type;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CmdCalculator {

	private static final String KEY_EQUATION = "equation";

	public static LiteralArgumentBuilder<FabricClientCommandSource> getBuilder() {
		return literal("=")
				.then(argument(KEY_EQUATION, StringArgumentType.greedyString())
						.executes(CmdCalculator::executesCalculate));
	}

	static int executesCalculate(CommandContext<FabricClientCommandSource> ctx) {
		String equation = StringArgumentType.getString(ctx, KEY_EQUATION);
		Text feedback;
		try {
			Type type = Calculator.calculate(equation);
			feedback = Text.translatable("text.minequery.calculator.done", Formatting.GRAY + equation, Formatting.GREEN + String.valueOf(type));
		} catch(Exception ex) {
			feedback = Text.translatable("text.minequery.calculator.error", "" + Formatting.RED + Formatting.ITALIC + ex.getMessage());
		}
		ctx.getSource().sendFeedback(feedback);
		return Command.SINGLE_SUCCESS;
	}

}
