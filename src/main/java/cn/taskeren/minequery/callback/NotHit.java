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

	public static class IronGolem implements AttackEntityCallback {

		public static final IronGolem INSTANCE = new IronGolem();

		private IronGolem() {}

		public enum CanDamage {
			ALL,
			NATURAL_ONLY,
			PLAYER_ONLY,
			NONE
		}

		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if(!config().notHitConfig.notHit) {
				return ActionResult.PASS;
			}
			if(entity instanceof IronGolemEntity) {
				boolean playerCreated = ((IronGolemEntity) entity).isPlayerCreated();
				switch(config().notHitConfig.ironGolem) {
					case ALL -> {
						return ActionResult.PASS;
					}
					case NATURAL_ONLY -> {
						return playerCreated ? ActionResult.FAIL : ActionResult.PASS;
					}
					case PLAYER_ONLY -> {
						return playerCreated ? ActionResult.PASS : ActionResult.FAIL;
					}
					case NONE -> {
						return ActionResult.FAIL;
					}
				}
			}
			return ActionResult.PASS;
		}
	}

	public static class Villager implements AttackEntityCallback {

		public static final Villager INSTANCE = new Villager();

		private Villager() {}

		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
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
