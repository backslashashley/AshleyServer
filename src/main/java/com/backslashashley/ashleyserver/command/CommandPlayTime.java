package com.backslashashley.ashleyserver.command;

import com.backslashashley.ashleyserver.stat.ModdedStats;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public class CommandPlayTime extends CommandBase {
	@Override
	public String getName() {
		return "playtime";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "playtime [player]";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (!(source instanceof ServerPlayerEntity)) {
			throw new CommandException("Unknown " + source.getName() + " tried to run /playtime!");
		}

		ServerPlayerEntity player = asPlayer(source);
		if (args.length > 0) {
			final ServerPlayerEntity name = server.getPlayerManager().get(args[0]);
			if (name != null) {
				player = name;
			}
		}

		float minutesPlayedNoAFK = player.getStats().get(ModdedStats.MINUTES_PLAYED_NO_AFK) / 20f / 60f / 60f;
		float minutesAFK = player.getStats().get(ModdedStats.MINUTES_AFK) / 20f / 60f / 60f;
		float total = player.getStats().get(Stats.MINUTES_PLAYED) / 20f / 60f / 60f;

		BigDecimal minutesPlayedNoAFK_bd = new BigDecimal(minutesPlayedNoAFK);
		BigDecimal roundedPlaytime = minutesPlayedNoAFK_bd.setScale(2, RoundingMode.HALF_EVEN);

		BigDecimal minutesAFK_bd = new BigDecimal(minutesAFK);
		BigDecimal roundedAFK = minutesAFK_bd.setScale(2, RoundingMode.HALF_EVEN);

		BigDecimal total_bd = new BigDecimal(total);
		BigDecimal roundedTotal = total_bd.setScale(2, RoundingMode.HALF_EVEN);

		source.sendMessage(new LiteralText(player.getName() + " Playtime Data"));
		source.sendMessage(new LiteralText("Hours played: " + roundedPlaytime));
		source.sendMessage(new LiteralText("Hours afk: " + roundedAFK));
		source.sendMessage(new LiteralText("Hours total: " + roundedTotal));
	}

	@Override
	public List<String> getSuggestions(MinecraftServer server, CommandSource source, String[] args, BlockPos targetPos) {
		return args.length == 1 ? suggestMatching(args, server.getPlayerNames()) : Collections.emptyList();
	}
}
