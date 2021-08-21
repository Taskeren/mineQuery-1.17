package cn.taskeren.minequery.callback;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.List;

import static cn.taskeren.minequery.MineQueryMod.config;

public class FeedEm {

	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(FeedEm::tick);
	}

	private static final List<AnimalEntity> FED_ENTITIES = Lists.newArrayList();

	private static void tick(MinecraftClient client) {
		if(!config().feedEm) {
			return;
		}

		ClientPlayerEntity cp = client.player;
		if(cp == null) {
			return;
		}

		if(cp.isSneaking()) {
			ItemStack stack = cp.getMainHandStack();
			if(stack.isEmpty() || stack.getItem() instanceof SpawnEggItem) { // 手里不能没东西，也不能是刷怪蛋
				return;
			}

			List<AnimalEntity> animals = cp.getEntityWorld().getEntitiesByClass(AnimalEntity.class,
					Box.of(cp.getPos(), 8, 8, 8), (animal) -> !animal.isBaby());

			for (AnimalEntity animal : animals) {
				if(!FED_ENTITIES.contains(animal)) {
					animal.setInvisible(true);
					FED_ENTITIES.add(animal);
					cp.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(animal, false, Hand.MAIN_HAND));
				}
			}
		} else {
			FED_ENTITIES.forEach(entity -> entity.setInvisible(false));
			FED_ENTITIES.clear();
		}
	}

}
