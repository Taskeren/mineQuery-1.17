package cn.taskeren.minequery.callback;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static cn.taskeren.minequery.MineQueryMod.config;

public class NotHit {

	private NotHit() {}

	public static void register() {
		AttackEntityCallback.EVENT.register(IronGolem::interact);
		AttackEntityCallback.EVENT.register(Villager::interact);
	}

	public static class IronGolem {

		private IronGolem() {}

		public enum PreventDamage {
			ALL,
			NATURAL_ONLY,
			PLAYER_ONLY,
			NONE
		}

		public static ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if(!config().notHitConfig.notHit) {
				return ActionResult.PASS;
			}
			if(entity instanceof IronGolemEntity) {
				boolean playerCreated = ((IronGolemEntity) entity).isPlayerCreated();
				switch(config().notHitConfig.ironGolem) {
					case ALL -> {
						return ActionResult.FAIL;
					}
					case NATURAL_ONLY -> {
						return playerCreated ? ActionResult.PASS : ActionResult.FAIL;
					}
					case PLAYER_ONLY -> {
						return playerCreated ? ActionResult.FAIL : ActionResult.PASS;
					}
					case NONE -> {
						return ActionResult.PASS;
					}
				}
			}
			return ActionResult.PASS;
		}
	}

	public static class Villager {

		private Villager() {}
		public static ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if(!config().notHitConfig.notHit) {
				return ActionResult.PASS;
			}
			if(entity instanceof VillagerEntity) {
				return config().notHitConfig.villager ? ActionResult.FAIL : ActionResult.PASS;
			}
			return ActionResult.PASS;
		}
	}

}
