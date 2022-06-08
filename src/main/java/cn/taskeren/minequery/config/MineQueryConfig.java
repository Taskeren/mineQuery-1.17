package cn.taskeren.minequery.config;

import cn.taskeren.minequery.MineQueryMod;
import cn.taskeren.minequery.callback.NotHit.IronGolem.PreventDamage;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.EnumMap;

import static cn.taskeren.minequery.key.KeyToCommand.KEY_BINDING_SIZE;

@Config(name = MineQueryMod.MOD_ID)
public class MineQueryConfig implements ConfigData {

	public boolean reseeding = true;
	public boolean autoRevive = true;

	public boolean feedEm = true;

	@ConfigEntry.Gui.CollapsibleObject
	public HarvestXConfig harvestXConfig = new HarvestXConfig();

	@ConfigEntry.Gui.CollapsibleObject
	public NotHitConfig notHitConfig = new NotHitConfig();

	@ConfigEntry.Gui.Excluded
	public NotPlaceConfig notPlaceConfig = new NotPlaceConfig();

	@ConfigEntry.BoundedDiscrete(max = KEY_BINDING_SIZE)
	public ArrayList<String> key2Cmd = new ArrayList<>();

	public static class HarvestXConfig {
		public boolean harvestX = true;
		public boolean checkCrops = true;
		public boolean checkNetherWart = true;
		public boolean checkCactus = true;
		public boolean checkSugarCane = true;
		public boolean checkStem = true;
		public boolean disableOnSneaking = true;
	}

	public static class NotHitConfig {
		public boolean notHit = true;

		@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
		public PreventDamage ironGolem = PreventDamage.ALL;

		public boolean villager = true;
	}

	/**
	 * Use NotPlacingConfig#isPrevented to check. True to prevent.
	 */
	public static class NotPlaceConfig {
		public EnumMap<Direction, Boolean> map = new EnumMap<>(Direction.class);

		public boolean isPrevented(Direction direction) {
			return map.getOrDefault(direction, false);
		}

		public void setPrevented(Direction direction, boolean value) {
			map.put(direction, value);
		}

		public boolean toggle(Direction direction) {
			boolean flag = !isPrevented(direction);
			setPrevented(direction, flag);
			return flag;
		}
	}

}
