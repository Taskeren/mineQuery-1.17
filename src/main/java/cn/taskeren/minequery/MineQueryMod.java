package cn.taskeren.minequery;

import cn.taskeren.minequery.callback.HarvestCheck;
import cn.taskeren.minequery.callback.NotHit;
import cn.taskeren.minequery.callback.NotPlace;
import cn.taskeren.minequery.command.CmdCalculator;
import cn.taskeren.minequery.command.CmdLocationCalc;
import cn.taskeren.minequery.config.MineQueryConfig;
import cn.taskeren.minequery.key.FeedEm;
import cn.taskeren.minequery.key.KeyToCommand;
import cn.taskeren.minequery.key.ModKeys;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MineQueryMod implements ClientModInitializer {

	public static final String MOD_ID = "minequery";
	public static final Logger LOGGER = LogManager.getLogger("Minequery");

	public static MineQueryConfig config() {
		return AutoConfig.getConfigHolder(MineQueryConfig.class).getConfig();
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Minequery!");

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			LOGGER.warn("Using Development Environment");
		}

		useConfig();

		registerCallbacks();
		registerCommands();

		ModKeys.init();
		KeyToCommand.init();
		FeedEm.init();
	}

	static void useConfig() {
		AutoConfig.register(MineQueryConfig.class, JanksonConfigSerializer::new);
	}

	static void registerCallbacks() {
		HarvestCheck.INSTANCE.register();
		NotHit.INSTANCE.register();
		NotPlace.INSTANCE.register();
	}

	static void registerCommands() {
		CmdCalculator.register();
		CmdLocationCalc.register();
	}

}

