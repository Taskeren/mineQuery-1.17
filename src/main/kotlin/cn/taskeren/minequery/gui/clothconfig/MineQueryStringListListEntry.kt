@file:Suppress("UnstableApiUsage")

package cn.taskeren.minequery.gui.clothconfig

import cn.taskeren.minequery.gui.clothconfig.MineQueryStringListListEntry.MineQueryStringListCell
import me.shedaniel.clothconfig2.gui.entries.AbstractTextFieldListListEntry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text
import java.util.*
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * This is the modified version of [StringListListEntry], which its cells are additionally rendering a indexing label,
 * to prevent the whole widget rendering nothing when the value is empty.
 */
class MineQueryStringListListEntry(
	fieldName: Text,
	value: MutableList<String>,
	defaultExpanded: Boolean,
	tooltipSupplier: Supplier<Optional<Array<Text>>>,
	saveConsumer: Consumer<MutableList<String>>,
	defaultValue: Supplier<MutableList<String>>,
	resetButtonKey: Text?,
	requiresRestart: Boolean,
	deleteButtonEnabled: Boolean,
	insertInFront: Boolean,
	createNewCell: BiFunction<String?, MineQueryStringListListEntry?, MineQueryStringListCell?>?,
) : AbstractTextFieldListListEntry<String, MineQueryStringListCell?, MineQueryStringListListEntry?>(
	fieldName,
	value,
	defaultExpanded,
	tooltipSupplier,
	saveConsumer,
	defaultValue,
	resetButtonKey,
	requiresRestart,
	deleteButtonEnabled,
	insertInFront,
	createNewCell
) {
	@JvmField
	var maxCount = -1

	override fun self(): MineQueryStringListListEntry {
		return this
	}

	override fun isInsertButtonEnabled(): Boolean {
		return if(maxCount < 0) super.isInsertButtonEnabled() else maxCount > this.value.size
	}

	class MineQueryStringListCell(value: String?, listListEntry: MineQueryStringListListEntry?) :
		AbstractTextFieldListCell<String, MineQueryStringListCell?, MineQueryStringListListEntry?>(
			value,
			listListEntry
		) {
		override fun getValue(): String? {
			return widget.text
		}

		override fun substituteDefault(value: String?): String {
			return value ?: ""
		}

		override fun isValidText(s: String): Boolean {
			return true
		}

		override fun getError(): Optional<Text> {
			return Optional.empty()
		}

		private val index: Int
			get() {
				val valueChildren =
					listListEntry!!.children().stream().filter { x: Element? -> x is MineQueryStringListCell }
						.toList()
				for(i in valueChildren.indices) {
					if(valueChildren[i] === this) {
						return i
					}
				}
				return -1
			}

		override fun render(
			graphics: DrawContext,
			index: Int,
			y: Int,
			x: Int,
			entryWidth: Int,
			entryHeight: Int,
			mouseX: Int,
			mouseY: Int,
			isSelected: Boolean,
			delta: Float,
		) {
			super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta)

			// add additional label
			val elIndex = this.index
			val indexText = if(elIndex == -1) "U" else (elIndex + 1).toString()
			val labelWidget =
				TextWidget(x - 20, y - 4, 20, 20, Text.of(indexText), MinecraftClient.getInstance().textRenderer)
			labelWidget.render(graphics, mouseX, mouseY, delta)
		}
	}
}
