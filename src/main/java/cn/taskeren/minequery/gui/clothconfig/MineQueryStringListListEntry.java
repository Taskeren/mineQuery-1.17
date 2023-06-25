package cn.taskeren.minequery.gui.clothconfig;

import me.shedaniel.clothconfig2.gui.entries.AbstractTextFieldListListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This is the modified version of {@link StringListListEntry}, which its cells are additionally rendering a indexing label,
 * to prevent the whole widget rendering nothing when the value is empty.
 */
@SuppressWarnings("UnstableApiUsage")
public class MineQueryStringListListEntry extends AbstractTextFieldListListEntry<String, MineQueryStringListListEntry.MineQueryStringListCell, MineQueryStringListListEntry> {

	public int maxCount = -1;

	public MineQueryStringListListEntry(Text fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Text[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Text resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<String, MineQueryStringListListEntry, MineQueryStringListCell> createNewCell) {
		super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, createNewCell);
	}

	@Override
	public MineQueryStringListListEntry self() {
		return this;
	}

	@Override
	public boolean isInsertButtonEnabled() {
		return maxCount < 0 ? super.isInsertButtonEnabled() : maxCount > this.getValue().size();
	}

	public static class MineQueryStringListCell extends AbstractTextFieldListCell<String, MineQueryStringListCell, MineQueryStringListListEntry> {

		public MineQueryStringListCell(@Nullable String value, MineQueryStringListListEntry listListEntry) {
			super(value, listListEntry);
		}

		@Override
		public String getValue() {
			return this.widget.getText();
		}

		@Override
		protected @Nullable String substituteDefault(@Nullable String value) {
			return value == null ? "" : value;
		}

		@Override
		protected boolean isValidText(@NotNull String s) {
			return true;
		}

		@Override
		public Optional<Text> getError() {
			return Optional.empty();
		}

		protected int getIndex() {
			var valueChildren = listListEntry.children().stream().filter(x -> x instanceof MineQueryStringListCell).toList();
			for(int i = 0; i < valueChildren.size(); i++) {
				if(valueChildren.get(i) == this) {
					return i;
				}
			}
			return -1;
		}

		@Override
		public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
			super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);

			// add additional label
			var elIndex = getIndex();
			var indexText = elIndex == -1 ? "U" : String.valueOf(elIndex + 1);
			var labelWidget = new TextWidget(x - 20, y - 4, 20, 20, Text.of(indexText), MinecraftClient.getInstance().textRenderer);
			labelWidget.render(graphics, mouseX, mouseY, delta);
		}
	}

}
