package cn.taskeren.minequery.features

import cn.taskeren.minequery.MineQuery
import cn.taskeren.minequery.config.PreventDamageIronGolem
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.util.ActionResult

object NotHit {

	private val c get() = MineQuery.config.notHitConfig

	fun init() {

		AttackEntityCallback.EVENT.register { _, _, _, entity, _ ->
			if(!c.notHit) {
				return@register ActionResult.PASS
			}

			if(entity is IronGolemEntity) {
				val isPlayerCreated = entity.isPlayerCreated
				when(c.ironGolem) {
					PreventDamageIronGolem.ALL -> {
						return@register ActionResult.FAIL
					}
					PreventDamageIronGolem.NATURAL_ONLY -> {
						if(!isPlayerCreated) {
							return@register ActionResult.FAIL
						}
					}
					PreventDamageIronGolem.PLAYER_ONLY -> {
						if(isPlayerCreated) {
							return@register ActionResult.FAIL
						}
					}
					PreventDamageIronGolem.NONE -> {
						return@register ActionResult.PASS
					}
				}
			}

			if(entity is VillagerEntity) {
				if(c.villager) {
					return@register ActionResult.FAIL
				}
			}

			ActionResult.PASS
		}

	}

}