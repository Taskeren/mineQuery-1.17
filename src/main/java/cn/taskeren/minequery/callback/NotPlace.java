package cn.taskeren.minequery.callback;

import cn.taskeren.minequery.config.MineQueryConfig;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

import static cn.taskeren.minequery.MineQueryMod.config;

public class NotPlace {

	private NotPlace() {}

	public static void register() {
		UseBlockCallback.EVENT.register(NotPlace::interact);
		ItemTooltipCallback.EVENT.register(NotPlace::getTooltip);
	}

	private static boolean validateItemStack(ItemStack stack) {
		return stack.isOf(Items.STICK) && stack.getName().getString().equalsIgnoreCase("NotPlace");
	}

	public static ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if(hand != Hand.MAIN_HAND || world.isClient) {
			return ActionResult.PASS;
		}

		MineQueryConfig.NotPlaceConfig opt = config().notPlaceConfig;

		ItemStack is = player.getStackInHand(hand);
		Direction direction = hitResult.getSide();
		if(validateItemStack(is) && player.isSneaking()) {
			var flag = opt.toggle(direction);
			player.sendMessage(Text.translatable("text.minequery.notplace.toggle."+(flag ? "prevented" : "allowed"), direction), false);
			return ActionResult.SUCCESS;
		}
		else if(is.getItem() instanceof BlockItem) {
			if(opt.isPrevented(direction)) {
				return ActionResult.FAIL;
			}
		}
		return ActionResult.PASS;
	}

	public static void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		if(validateItemStack(stack)) {
			MineQueryConfig.NotPlaceConfig opt = config().notPlaceConfig;
			for(var direction : Direction.values()) {
				var prv = opt.isPrevented(direction);
				lines.add(Text.translatable("text.minequery.notplace.tooltip."+(prv ? "prevented" : "allowed"),
						direction.getName()));
			}
		}
	}
}
