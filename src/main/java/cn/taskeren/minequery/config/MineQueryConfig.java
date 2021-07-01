package cn.taskeren.minequery.config;

import cn.taskeren.minequery.MineQueryMod;
import cn.taskeren.minequery.callback.NotHit.IronGolem.CanDamage;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;

import static cn.taskeren.minequery.key.KeyToCommand.KEY_BINDING_SIZE;

@Config(name = MineQueryMod.MOD_ID)
public class MineQueryConfig implements ConfigData {

	public boolean reseeding = true;
	public boolean autoRevive = true;

	@ConfigEntry.Gui.CollapsibleObject
	public HarvestXConfig harvestXConfig = new HarvestXConfig();

	@ConfigEntry.Gui.CollapsibleObject
	public NotHitConfig notHitConfig = new NotHitConfig();

	@ConfigEntry.BoundedDiscrete(max = KEY_BINDING_SIZE)
	public ArrayList<String> key2Cmd = new ArrayList<>();

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

		@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
		public CanDamage ironGolem = CanDamage.ALL;

		public boolean villager = true;
	}

}
