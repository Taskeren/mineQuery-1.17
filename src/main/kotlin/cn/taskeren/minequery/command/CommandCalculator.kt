package cn.taskeren.minequery.command

import cn.taskeren.brigadierx.argument
import cn.taskeren.brigadierx.executesX
import cn.taskeren.brigadierx.newLiteralArgBuilder
import com.mojang.brigadier.arguments.StringArgumentType
import io.github.endreman0.calculator.Calculator
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

private const val KEY_EQUATION = "equation"

internal val CommandCalculator = newLiteralArgBuilder<FabricClientCommandSource>("=") {
	argument(KEY_EQUATION, StringArgumentType.greedyString()) {
		executesX { ctx ->
			val equation = StringArgumentType.getString(ctx, KEY_EQUATION)
			val feedback = runCatching {
				val type = Calculator.calculate(equation)
				Text.translatable(
					"text.minequery.calculator.done", "${Formatting.GRAY}$equation", "${Formatting.GREEN}${type}"
				)
			}.getOrElse {
				Text.translatable(
					"text.minequery.calculator.error", "${Formatting.RED}${Formatting.ITALIC}${it.message}"
				)
			}
			ctx.source.sendFeedback(feedback)
		}
	}
}
