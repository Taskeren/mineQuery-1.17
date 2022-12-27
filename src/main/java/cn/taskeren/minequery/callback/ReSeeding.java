package cn.taskeren.minequery.callback;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static cn.taskeren.minequery.MineQueryMod.config;

public class ReSeeding {

	private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(16);

	private ReSeeding() {}

	public static void schedule(PlayerEntity player, BlockPos pos) {
		// 使用 RightBreak 时禁用 ReSeeding
		if(!config().harvestXConfig.reseeding || (config().harvestXConfig.rightBreak)) {
			return;
		}

		EXECUTORS.submit(() -> {
			BlockPos bpos = pos.down();
			BlockHitResult bhr = new BlockHitResult(Vec3d.ofCenter(bpos), Direction.UP, bpos, false);
			PlayerInteractBlockC2SPacket packet = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0);

			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			} finally {
				if(player instanceof ClientPlayerEntity) {
					((ClientPlayerEntity) player).networkHandler.sendPacket(packet);
				}
			}
		});
	}
}
