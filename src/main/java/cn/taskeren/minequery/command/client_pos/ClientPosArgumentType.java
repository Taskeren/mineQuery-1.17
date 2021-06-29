package cn.taskeren.minequery.command.client_pos;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColumnPos;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class ClientPosArgumentType implements ArgumentType<ClientPosArgument> {

	private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~1 ~-2", "^ ^", "^-1 ^0");
	public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("argument.pos2d.incomplete"));

	public static ClientPosArgumentType columnPos() {
		return new ClientPosArgumentType();
	}

	public static ColumnPos getColumnPos(CommandContext<FabricClientCommandSource> context, String name) {
		BlockPos blockPos = context.getArgument(name, ClientPosArgument.class).toAbsoluteBlockPos(context.getSource());
		return new ColumnPos(blockPos.getX(), blockPos.getZ());
	}

	@Override
	public ClientPosArgument parse(StringReader reader) throws CommandSyntaxException {
		int i = reader.getCursor();
		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		} else {
			CoordinateArgument coordinateArgument = CoordinateArgument.parse(reader);
			if (reader.canRead() && reader.peek() == ' ') {
				reader.skip();
				CoordinateArgument coordinateArgument2 = CoordinateArgument.parse(reader);
				return new ClientDefaultPosArgument(coordinateArgument, new CoordinateArgument(true, 0.0D), coordinateArgument2);
			} else {
				reader.setCursor(i);
				throw INCOMPLETE_EXCEPTION.createWithContext(reader);
			}
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		if (!(context.getSource() instanceof CommandSource)) {
			return Suggestions.empty();
		} else {
			String string = builder.getRemaining();
			Collection<CommandSource.RelativePosition> collection2;
			if (!string.isEmpty() && string.charAt(0) == '^') {
				collection2 = Collections.singleton(CommandSource.RelativePosition.ZERO_LOCAL);
			} else {
				collection2 = ((CommandSource)context.getSource()).getBlockPositionSuggestions();
			}

			return CommandSource.suggestColumnPositions(string, collection2, builder, CommandManager.getCommandValidator(this::parse));
		}
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
