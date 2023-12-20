package cn.taskeren.minequery.config

import cn.taskeren.minequery.MineQuery
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded
import net.minecraft.util.math.Direction
import java.util.EnumMap

@Config(name = MineQuery.MOD_ID)
class MineQueryConfig(
	var autoRevive: Boolean = true,
	var feedEm: Boolean = true,

	@CollapsibleObject
	var harvestXConfig: HarvestXConfig = HarvestXConfig(),

	@CollapsibleObject
	var notHitConfig: NotHitConfig = NotHitConfig(),

	@Excluded
	var notPlaceConfig: NotPlaceConfig = NotPlaceConfig(),

	@Excluded
	val key2Cmd: MutableList<String> = mutableListOf(),
) : ConfigData {

}

data class HarvestXConfig(
	var harvestX: Boolean = true,
	var checkCrops: Boolean = true,
	var checkNetherWart: Boolean = true,
	var checkCactus: Boolean = true,
	var checkSugarCane: Boolean = true,
	var checkStem: Boolean = true,
	var disableOnSneaking: Boolean = true,
	var reseeding: Boolean = true,
	var rightBreak: Boolean = true,
)

data class NotHitConfig(
	var notHit: Boolean = true,
	@EnumHandler(option = EnumDisplayOption.BUTTON)
	var ironGolem: PreventDamageIronGolem = PreventDamageIronGolem.ALL,
	var villager: Boolean = true,
)

enum class PreventDamageIronGolem {
	ALL,
	NATURAL_ONLY,
	PLAYER_ONLY,
	NONE,
}

data class NotPlaceConfig(
	val map: EnumMap<Direction, Boolean> = EnumMap(Direction::class.java)
) {
	fun isPrevented(direction: Direction) = map[direction] ?: false

	fun setPrevented(direction: Direction, value: Boolean) = map.put(direction, value)

	fun toggle(direction: Direction): Boolean {
		val flag = !isPrevented(direction)
		setPrevented(direction, flag)
		return flag
	}
}
