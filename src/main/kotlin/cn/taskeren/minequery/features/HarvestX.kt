package cn.taskeren.minequery.features

import cn.taskeren.minequery.MineQuery
import kotlinx.coroutines.*
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Blocks
import net.minecraft.block.CactusBlock
import net.minecraft.block.CropBlock
import net.minecraft.block.NetherWartBlock
import net.minecraft.block.StemBlock
import net.minecraft.block.SugarCaneBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import org.slf4j.MarkerFactory
import java.util.concurrent.Executors

object HarvestX {

	private val c get() = MineQuery.config.harvestXConfig

	fun init() {
		// register callback
		AttackBlockCallback.EVENT.register { player, world, _, pos, _ ->
			if(!c.harvestX) {
				return@register ActionResult.PASS
			}
			if(c.disableOnSneaking && player.isSneaking) {
				return@register ActionResult.PASS
			}

			val state = world.getBlockState(pos)
			val block = state.block

			when {
				(block is CropBlock && c.checkCrops) -> { // normal crop blocks
					val age = block.getAge(state)
					val max = block.maxAge

					if(age != max) {
						return@register ActionResult.FAIL
					} else {
						reseed(player, pos)
					}
				}

				(block is NetherWartBlock && c.checkNetherWart) -> {
					val age = state.get(NetherWartBlock.AGE)
					if(age != 3) {
						return@register ActionResult.FAIL
					} else {
						reseed(player, pos)
					}
				}

				(block is CactusBlock && c.checkCactus) -> {
					if(world.getBlockState(pos.down()).block != Blocks.CACTUS) {
						return@register ActionResult.FAIL
					}
				}

				(block is SugarCaneBlock && c.checkSugarCane) -> {
					if(world.getBlockState(pos.down()).block != Blocks.SUGAR_CANE) {
						return@register ActionResult.FAIL
					}
				}

				(block is StemBlock && c.checkStem) -> { // never break stems
					return@register ActionResult.FAIL
				}

				else -> return@register ActionResult.PASS
			}

			ActionResult.PASS
		}

		// the harvest mod like
		UseBlockCallback.EVENT.register { player, world, _, hitResult ->
			if(!c.rightBreak || player.isSpectator) {
				return@register ActionResult.PASS
			}

			val pos = hitResult.blockPos
			val side = hitResult.side
			val state = world.getBlockState(pos)

			if(c.checkCrops) {
				val block = state.block
				if(block is CropBlock) {
					val age = block.getAge(state)
					val max = block.maxAge
					if(age == max) {
						attackAndSwingHand(pos, side)
					}
				}
			}

			if(c.checkNetherWart) {
				val block = state.block
				if(block is NetherWartBlock) {
					val age = state.get(NetherWartBlock.AGE)
					if(age == 3) {
						attackAndSwingHand(pos, side)
					}
				}
			}

			ActionResult.PASS
		}
	}

	private val reseedingMarker = MarkerFactory.getMarker("ReSeeding")
	private val reseedingDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

	private fun reseed(player: PlayerEntity, pos: BlockPos) {
		if(!c.reseeding || c.rightBreak) {
			return
		}

		runBlocking(reseedingDispatcher) {
			launch {
				val p = pos.down()
				val bhr = BlockHitResult(Vec3d.ofCenter(p), Direction.UP, p, false)
				val pkt = PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0)

				delay(5)

				if(player is ClientPlayerEntity) {
					player.networkHandler.sendPacket(pkt)
				} else {
					MineQuery.logger.warn(reseedingMarker, "Invalid player type: ${player::class.java}. This must be something wrong!")
				}
			}
		}
	}

	private fun attackAndSwingHand(pos: BlockPos, side: Direction) {
		// send an attack packet
		val interactionMgr = (MinecraftClient.getInstance() ?: return).interactionManager ?: return
		interactionMgr.attackBlock(pos, side)

		// play attack animation
		val player = (MinecraftClient.getInstance() ?: return).player ?: return
		player.swingHand(Hand.MAIN_HAND)
	}

}