package cn.taskeren.minequery.callback;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static cn.taskeren.minequery.MineQueryMod.config;

public class HarvestCheck {

	public static void register() {
		AttackBlockCallback.EVENT.register(HarvestCheck::interact);
	}

	public static ActionResult interact(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
		if(!config().harvestXConfig.harvestX || (config().harvestXConfig.disableOnSneaking && player.isSneaking())) {
			return ActionResult.PASS;
		}

		BlockState state = world.getBlockState(pos);
		if(config().harvestXConfig.checkCrops) {
			if(state.getBlock() instanceof CropBlock crop) {
				int age = crop.getAge(state);
				int max = crop.getMaxAge();
				if(age != max) {
					return ActionResult.FAIL;
				}
				else {
					ReSeeding.schedule(player, pos);
				}
			}
		}

		if(config().harvestXConfig.checkNetherWart) {
			if(state.getBlock() instanceof NetherWartBlock) { // fix #1
				int age = state.get(NetherWartBlock.AGE);
				if(age != 3) {
					return ActionResult.FAIL;
				}
				else {
					ReSeeding.schedule(player, pos);
				}
			}
		}

		if(config().harvestXConfig.checkCactus) {
			if(state.getBlock() == Blocks.CACTUS) {
				if(world.getBlockState(pos.down()).getBlock() != Blocks.CACTUS) { // Has No Cactus below
					return ActionResult.FAIL;
				}
			}
		}

		if(config().harvestXConfig.checkSugarCane) {
			if(state.getBlock() == Blocks.SUGAR_CANE) {
				if(world.getBlockState(pos.down()).getBlock() != Blocks.SUGAR_CANE) { // Has No Sugar Cane below
					return ActionResult.FAIL;
				}
			}
		}

		if(config().harvestXConfig.checkStem) {
			if(state.getBlock() instanceof StemBlock) {
				return ActionResult.FAIL;
			}
		}


		return ActionResult.PASS;
	}
}
