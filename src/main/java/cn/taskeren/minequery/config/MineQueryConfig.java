package cn.taskeren.minequery.config;

import cn.taskeren.minequery.MineQueryMod;
import cn.taskeren.minequery.callback.NotHit.IronGolem.CanDamage;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = MineQueryMod.MOD_ID)
public class MineQueryConfig implements ConfigData {

	public boolean reseeding = true;
	public boolean autoRevive = true;

	@ConfigEntry.Gui.CollapsibleObject
	public HarvestXConfig harvestXConfig = new HarvestXConfig();

	@ConfigEntry.Gui.CollapsibleObject
	public NotHitConfig notHitConfig = new NotHitConfig();

	public static class HarvestXConfig {
		public boolean harvestX = true;
		public boolean checkCrops      = true;
		public boolean checkNetherWart = true;
		public boolean checkCactus     = true;
		public boolean checkSugarCane  = true;
		public boolean checkStem       = true;
	}

	public static class NotHitConfig {
		public boolean notHit    = true;

		@ConfigEntry.Gui.EnumHandler
		public CanDamage ironGolem = CanDamage.ALL;

		public boolean villager = true;
	}

}
