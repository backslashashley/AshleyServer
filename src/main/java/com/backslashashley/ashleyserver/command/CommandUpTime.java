package com.backslashashley.ashleyserver.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.LiteralText;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CommandUpTime extends CommandBase {
	@Override
	public String getName() {
		return "uptime";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "uptime";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		float timeInHours = server.getTicks() / 20f / 60f / 60f;
		BigDecimal a = new BigDecimal(timeInHours);
		BigDecimal roundedTimeInHours = a.setScale(2, RoundingMode.HALF_EVEN);

		source.sendMessage(new LiteralText(roundedTimeInHours + " hours"));
	}
}
