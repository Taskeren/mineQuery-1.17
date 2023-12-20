package cn.taskeren.minequery.features

import cn.taskeren.minequery.MQKeyRegistry
import cn.taskeren.minequery.MineQuery
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.item.SpawnEggItem
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
import net.minecraft.util.Hand
import net.minecraft.util.math.Box

object FeedEm {

	private val fedList = mutableListOf<Entity>()

	fun init() {

		ClientTickEvents.END_CLIENT_TICK.register { client ->
			if(!MineQuery.config.feedEm) {
				return@register
			}

			if(MQKeyRegistry.keyFeedEm.isPressed) {
				val cp = client.player ?: return@register

				val stack = cp.mainHandStack
				if(stack.isEmpty || stack.item is SpawnEggItem) {
					return@register
				}

				val animals = cp.entityWorld.getEntitiesByClass(AnimalEntity::class.java, Box.of(cp.pos, 8.0, 8.0, 8.0)) { !it.isBaby }
				animals.forEach {
					if(it !in fedList) {
						it.isInvisible = true
						fedList += it
						cp.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(it, false, Hand.MAIN_HAND))
					}
				}
			} else {
				fedList.forEach { it.isInvisible = false }
				fedList.clear()
			}
		}

	}

}