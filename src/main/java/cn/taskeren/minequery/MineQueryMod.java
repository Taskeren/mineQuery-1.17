package cn.taskeren.minequery;

import cn.taskeren.minequery.callback.HarvestCheck;
import cn.taskeren.minequery.callback.NotHit;
import cn.taskeren.minequery.callback.NotPlace;
import cn.taskeren.minequery.command.CmdCalculator;
import cn.taskeren.minequery.command.CmdLocationCalc;
import cn.taskeren.minequery.config.MineQueryConfig;
import cn.taskeren.minequery.key.KeyToCommand;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;

public class MineQueryMod implements ClientModInitializer {

	public static final String MOD_ID = "minequery";

	public static MineQueryConfig config() {
		return AutoConfig.getConfigHolder(MineQueryConfig.class).getConfig();
	}

	@Override
	public void onInitializeClient() {
		System.out.println("MineQuery!");

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			System.out.println("Hopeless");
		}

		AutoConfig.register(MineQueryConfig.class, JanksonConfigSerializer::new);

		AttackBlockCallback.EVENT.register(HarvestCheck.INSTANCE);
		AttackEntityCallback.EVENT.register(NotHit.IronGolem.INSTANCE);
		AttackEntityCallback.EVENT.register(NotHit.Villager.INSTANCE);

		UseBlockCallback.EVENT.register(NotPlace.INSTANCE);
		ItemTooltipCallback.EVENT.register(NotPlace.INSTANCE);

		CmdCalculator.register();
		CmdLocationCalc.register();

		KeyToCommand.init();
	}

}

