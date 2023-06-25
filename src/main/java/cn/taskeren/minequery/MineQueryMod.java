package cn.taskeren.minequery;

import cn.taskeren.minequery.callback.HarvestCheck;
import cn.taskeren.minequery.callback.HarvestModLike;
import cn.taskeren.minequery.callback.NotHit;
import cn.taskeren.minequery.callback.NotPlace;
import cn.taskeren.minequery.command.CmdCalculator;
import cn.taskeren.minequery.command.CmdKey2CmdSettings;
import cn.taskeren.minequery.command.CmdLocationCalc;
import cn.taskeren.minequery.config.MineQueryConfig;
import cn.taskeren.minequery.key.FeedEm;
import cn.taskeren.minequery.key.KeyToCommand;
import cn.taskeren.minequery.key.ModKeys;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MineQueryMod implements ClientModInitializer {

	public static final String MOD_ID = "minequery";
	public static final Logger LOGGER = LogManager.getLogger("Minequery");

	public static MineQueryConfig config() {
		return AutoConfig.getConfigHolder(MineQueryConfig.class).getConfig();
	}

	public static void saveConfig() {
		AutoConfig.getConfigHolder(MineQueryConfig.class).save();
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Minequery!");

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			LOGGER.warn("Using Development Environment");
		}

		useConfig();

		registerCallbacks();

		// Register client commands
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
			dispatcher.register(CmdCalculator.getBuilder());
			dispatcher.register(CmdLocationCalc.getBuilder());
			dispatcher.register(CmdKey2CmdSettings.getBuilder());
		});

		ModKeys.init();
		KeyToCommand.init();
		FeedEm.init();
	}

	static void useConfig() {
		AutoConfig.register(MineQueryConfig.class, JanksonConfigSerializer::new);
	}

	static void registerCallbacks() {
		HarvestCheck.register();
		NotHit.register();
		NotPlace.register();
		HarvestModLike.register();
	}

}

