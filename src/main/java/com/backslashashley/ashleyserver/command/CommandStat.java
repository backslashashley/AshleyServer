package com.backslashashley.ashleyserver.command;

import com.backslashashley.ashleyserver.stat.ScoreboardHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.exception.CommandSyntaxException;
import net.minecraft.server.command.exception.IncorrectUsageException;
import net.minecraft.server.command.source.CommandExecutor;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.command.source.CommandSourceStack;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CommandStat extends CommandBase {
	@Override
	public String getName() {
		return "stat";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "stat <sidebar|belowname|list> <break|craft|drop|killedby|killed|mined|pickedup|used|extra|clear> [item]";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (!(source instanceof ServerPlayerEntity)) {
			throw new CommandException("Unknown " + source.getName() + " tried to run /stat!");
		}

		String command = "/scoreboard objectives setdisplay";
		String displaySlot;


		if (args.length < 2) {
			throw new IncorrectUsageException("Check usage");
		}

		if ("sidebar".equalsIgnoreCase(args[0]) || "list".equalsIgnoreCase(args[0]) || "belowname".equalsIgnoreCase(args[0])) {
			displaySlot = args[0];
			command += " " + displaySlot;
		} else {
			throw new IncorrectUsageException("Check usage");
		}

		if ("break".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardBreakShorthand);
			String criteria = ScoreboardHandler.scoreboardBreak[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("craft".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardCraftShorthand);
			String criteria = ScoreboardHandler.scoreboardCraft[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("drop".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardDropShorthand);
			String criteria = ScoreboardHandler.scoreboardDrop[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("killedby".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardKilledbyShorthand);
			String criteria = ScoreboardHandler.scoreboardKilledby[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("killed".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardKilledShorthand);
			String criteria = ScoreboardHandler.scoreboardKilled[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("mined".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardMineShorthand);
			String criteria = ScoreboardHandler.scoreboardMine[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("pickedup".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardPickupShorthand);
			String criteria = ScoreboardHandler.scoreboardPickup[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("used".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardUsedShorthand);
			String criteria = ScoreboardHandler.scoreboardUsed[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}
		if ("extra".equalsIgnoreCase(args[1])) {
			int i = indexOf(args[2], ScoreboardHandler.scoreboardExtraShorthand);
			String criteria = ScoreboardHandler.scoreboardExtra[i];
			int x = indexOf(criteria, ScoreboardHandler.scoreboardAll);
			command += " " + x;
		}

		CommandSourceStack sourceStack = new CommandSourceStack(source, null, null, null, null, false);
		server.commandHandler.run(sourceStack, command);
	}

	@Override
	public List<String> getSuggestions(MinecraftServer server, CommandSource source, String[] args, @Nullable BlockPos pos) {
		if (args.length == 1) {
			return suggestMatching(args, "sidebar", "list", "belowname");
		}
		if (args.length == 2) {
			return suggestMatching(args, "break", "craft", "drop", "killedby", "killed", "mined", "pickedup", "used", "extra", "clear");
		}

		if (args.length == 3) {
			if ("break".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardBreakShorthand);
			}
			if ("craft".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardCraftShorthand);
			}
			if ("drop".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardDropShorthand);
			}
			if ("killedby".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardKilledbyShorthand);
			}
			if ("killed".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardKilledShorthand);
			}
			if ("mined".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardMineShorthand);
			}
			if ("pickedup".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardPickupShorthand);
			}
			if ("used".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardUsedShorthand);
			}
			if ("extra".equalsIgnoreCase(args[1])) {
				return suggestMatching(args, ScoreboardHandler.scoreboardExtraShorthand);
			}
		}

		return Collections.emptyList();
	}

	public int indexOf(String element, String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase(element)) {
				return i;
			}
		}
		return -1;
	}
}
