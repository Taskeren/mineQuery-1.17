package cn.taskeren.minequery.callback;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static cn.taskeren.minequery.MineQueryMod.config;

public class HarvestModLike {

	private static void attackAndSwingHand(BlockPos pos, Direction side) {
		if(MinecraftClient.getInstance() != null) {
			ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
			if(interactionManager != null) {
				interactionManager.attackBlock(pos, side);
			}

			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if(player != null) {
				player.swingHand(Hand.MAIN_HAND);
			}
		}
	}

	public static void register() {
		UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) -> {
			if(!config().harvestXConfig.rightBreak || player.isSpectator()) {
				return ActionResult.PASS;
			}

			BlockPos pos = hitResult.getBlockPos();
			Direction side = hitResult.getSide();
			BlockState state = world.getBlockState(pos);

			if(config().harvestXConfig.checkCrops) {
				if(state.getBlock() instanceof CropBlock crop) {
					int age = crop.getAge(state);
					int max = crop.getMaxAge();
					if (age == max) {
						attackAndSwingHand(pos, side);
					}
				}
			}

			if(config().harvestXConfig.checkNetherWart) {
				if(state.getBlock() instanceof NetherWartBlock) { // fix #1
					int age = state.get(NetherWartBlock.AGE);
					if (age == 3) {
						attackAndSwingHand(pos, side);
					}
				}
			}

			return ActionResult.PASS;
		});
	}

}
