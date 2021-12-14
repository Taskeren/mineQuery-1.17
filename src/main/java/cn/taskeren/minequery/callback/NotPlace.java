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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

import static cn.taskeren.minequery.MineQueryMod.config;

public class NotPlace implements UseBlockCallback, ItemTooltipCallback, Registerable {

	public static final NotPlace INSTANCE = new NotPlace();

	private NotPlace() {}

	@Override
	public void register() {
		UseBlockCallback.EVENT.register(this);
		ItemTooltipCallback.EVENT.register(this);
	}

	private static boolean validateItemStack(ItemStack stack) {
		return stack.isOf(Items.STICK) && stack.getName().getString().equalsIgnoreCase("NotPlace");
	}

	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if(hand != Hand.MAIN_HAND || world.isClient) {
			return ActionResult.PASS;
		}

		MineQueryConfig.NotPlaceConfig opt = config().notPlaceConfig;

		ItemStack is = player.getStackInHand(hand);
		Direction direction = hitResult.getSide();
		if(validateItemStack(is) && player.isSneaking()) {
			var flag = opt.toggle(direction);
			player.sendMessage(new TranslatableText("text.minequery.notplace.toggle."+(flag ? "prevented" : "allowed"), direction), false);
			return ActionResult.SUCCESS;
		}
		else if(is.getItem() instanceof BlockItem) {
			if(opt.isPrevented(direction)) {
				return ActionResult.FAIL;
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		if(validateItemStack(stack)) {
			MineQueryConfig.NotPlaceConfig opt = config().notPlaceConfig;
			for(var direction : Direction.values()) {
				var prv = opt.isPrevented(direction);
				lines.add(new TranslatableText("text.minequery.notplace.tooltip."+(prv ? "prevented" : "allowed"),
						direction.getName()));
			}
		}
	}
}
