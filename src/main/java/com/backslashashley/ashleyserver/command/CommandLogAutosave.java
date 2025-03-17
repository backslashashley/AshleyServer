package com.backslashashley.ashleyserver.command;

import com.backslashashley.ashleyserver.AshleyServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class CommandLogAutosave extends CommandBase{
	@Override
	public String getName() {
		return "logsave";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "logsave";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (!(source instanceof ServerPlayerEntity)) {
			throw new CommandException("Unknown " + source.getName() + " tried to run /autosave!");
		}

		boolean log = !AshleyServer.logAutosave;
		AshleyServer.logAutosave = log;
		source.sendMessage(new LiteralText("" + log));
	}
}
