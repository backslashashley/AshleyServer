package com.backslashashley.ashleyserver.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class CommandCamera extends CommandBase {

	@Override
	public String getName() {
		return "c";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "c";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (!(source instanceof ServerPlayerEntity)) {
			throw new CommandException("Unknown " + source.getName() + " tried to run /c!");
		}

		ServerPlayerEntity player = asPlayer(source);

		if (!player.onGround && player.interactionManager.getGameMode() == GameMode.SURVIVAL) {
			throw new CommandException("Must be on solid ground");
		}
		if (player.isOnFire()) {
			throw new CommandException("Must not be on fire");
		}
		if (player.isInWall()) {
			throw new CommandException("Cannot be suffocating");
		}

		if (player.interactionManager.getGameMode() == GameMode.SURVIVAL) {
			player.setGameMode(GameMode.SPECTATOR);
		}
		else if (player.interactionManager.getGameMode() == GameMode.SPECTATOR) {
			player.setGameMode(GameMode.SURVIVAL);
		}

		//TODO Save position & rotation to prevent cheat
	}
}
