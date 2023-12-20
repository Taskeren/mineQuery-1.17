package cn.taskeren.minequery

import cn.taskeren.minequery.command.CommandCalculator
import cn.taskeren.minequery.command.CommandKey2CommandSettings
import cn.taskeren.minequery.command.CommandLocationCalculator
import cn.taskeren.minequery.config.MineQueryConfig
import cn.taskeren.minequery.features.*
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MineQuery : ClientModInitializer {

	const val MOD_ID = "minequery"
	const val MOD_NAME = "MineQuery"

	val logger: Logger = LoggerFactory.getLogger(MOD_NAME)
	val config: MineQueryConfig get() = configHolder.config

	private val configHolder get() = AutoConfig.getConfigHolder(MineQueryConfig::class.java)

	private var isDevEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment

	override fun onInitializeClient() {
		logger.info("Hello from Planet Earth!")

		if(isDevEnvironment) {
			logger.warn("In dev environment!")
		}

		// init config
		AutoConfig.register(MineQueryConfig::class.java, ::JanksonConfigSerializer)

		// register features
		HarvestX.init()
		NotHit.init()
		NotPlace.init()

		FeedEm.init()

		// register keys
		MQKeyRegistry.init()
		KeyToCommand.init()

		// register commands
		ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			dispatcher.register(CommandCalculator)
			dispatcher.register(CommandLocationCalculator)
			dispatcher.register(CommandKey2CommandSettings)
		}
	}

	fun saveConfig() {
		configHolder.save()
	}
}