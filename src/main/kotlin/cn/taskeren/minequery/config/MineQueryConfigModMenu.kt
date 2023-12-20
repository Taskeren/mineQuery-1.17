package cn.taskeren.minequery.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig

class MineQueryConfigModMenu : ModMenuApi {

	override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
		return ConfigScreenFactory { parent ->
			AutoConfig.getConfigScreen(MineQueryConfig::class.java, parent).get()
		}
	}
}