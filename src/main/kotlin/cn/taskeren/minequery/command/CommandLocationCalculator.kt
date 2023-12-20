package cn.taskeren.minequery.command

import cn.taskeren.brigadierx.argument
import cn.taskeren.brigadierx.executesX
import cn.taskeren.brigadierx.literal
import cn.taskeren.brigadierx.newLiteralArgBuilder
import cn.taskeren.minequery.command.client_pos.ClientPosArgumentType
import com.mojang.brigadier.Command
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text
import kotlin.math.floor

internal val CommandLocationCalculator =
	newLiteralArgBuilder<FabricClientCommandSource>("localc") {
		literal("toNether") {
			argument("location", ClientPosArgumentType.columnPos()) {
				executes {
					val pos = ClientPosArgumentType.getColumnPos(it, "location")
					val x = floor(pos.x.toDouble() / 8)
					val z = floor(pos.z.toDouble() / 8)
					it.source.sendFeedback(Text.translatable("text.minequery.localc.to_nether", x, z))
					Command.SINGLE_SUCCESS
				}
			}
		}

		literal("toOverworld") {
			argument("location", ClientPosArgumentType.columnPos()) {
				executesX {
					val pos = ClientPosArgumentType.getColumnPos(it, "location")
					val x = floor(pos.x.toDouble() * 8)
					val z = floor(pos.z.toDouble() * 8)
					it.source.sendFeedback(Text.translatable("text.minequery.localc.to_overworld", x, z))
					Command.SINGLE_SUCCESS
				}
			}
		}
	}