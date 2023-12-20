package cn.taskeren.minequery.features

import cn.taskeren.minequery.MineQuery
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Direction

object NotPlace {

	private val c get() = MineQuery.config.notPlaceConfig

	fun init() {
		UseBlockCallback.EVENT.register { player, world, hand, hitResult ->
			if(hand != Hand.MAIN_HAND || world.isClient) {
				return@register ActionResult.PASS
			}

			val stack = player.getStackInHand(hand)
			val direction = hitResult.side
			if(stack.isValidItemStack() && player.isSneaking) {
				val flag = c.toggle(direction)
				player.sendMessage(Text.translatable("text.minequery.notplace.toggle.${if(flag) "prevented" else "allowed"}", direction), false)
				return@register ActionResult.SUCCESS
			} else if(stack.item is BlockItem) {
				if(c.isPrevented(direction)) {
					return@register ActionResult.FAIL
				}
			}

			ActionResult.PASS
		}

		// register tooltip for the stick
		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			if(stack.isValidItemStack()) {
				Direction.entries.forEach {
					lines.add(Text.translatable("text.minequery.notplace.tooltip.${if(c.isPrevented(it)) "prevented" else "allowed"}", it.name))
				}
			}
		}
	}

	private fun ItemStack.isValidItemStack() =
		isOf(Items.STICK) && name.string.equals("NotPlace", true)

}