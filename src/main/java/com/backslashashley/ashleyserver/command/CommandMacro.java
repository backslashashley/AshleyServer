package com.backslashashley.ashleyserver.command;

import com.backslashashley.ashleyserver.AshleyServer;
import com.backslashashley.ashleyserver.util.PlayerActionHandler;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public class CommandMacro extends CommandBase {
	public String getName() {
		return "macro";
	}

	public String getUsage(CommandSource sender) {
		return "Usage: macro <attack|use> <once|continuous|interval";
	}

	public void run(MinecraftServer server, CommandSource sender, String[] args) throws CommandException {
		if (!(sender instanceof PlayerEntity)) {return;}

		ServerPlayerEntity playerMP = asPlayer(sender);

		String action = args[0];

		if ("use".equalsIgnoreCase(action) || "attack".equalsIgnoreCase(action)) {
			PlayerActionHandler playerActionHandlerGlobal = null;
			for (PlayerActionHandler playerActionHandler : AshleyServer.PLAYER_ACTION_HANDLERS) {
				if (playerActionHandler.player == playerMP) {
					playerActionHandlerGlobal = playerActionHandler;
				}
			}
			String option = "once";
			int interval = 0;
			if (args.length > 1) {
				option = args[1];
				if (args.length > 2 && option.equalsIgnoreCase("interval")) {
					interval = parseInt(args[2],2,72000);
				}
			}

			if (action.equalsIgnoreCase("use")) {
				if (option.equalsIgnoreCase("once"))
					playerActionHandlerGlobal.useOnce();
				if (option.equalsIgnoreCase("continuous"))
					playerActionHandlerGlobal.setUseForever();
				if (option.equalsIgnoreCase("interval") && interval > 1)
					playerActionHandlerGlobal.setUse(interval, 0);
			}
			if (action.equalsIgnoreCase("attack")) {
				if (option.equalsIgnoreCase("once"))
					playerActionHandlerGlobal.attackOnce();
				if (option.equalsIgnoreCase("continuous"))
					playerActionHandlerGlobal.setAttackForever();
				if (option.equalsIgnoreCase("interval") && interval > 1)
					playerActionHandlerGlobal.setAttack(interval, 0);
			}
		}
		if ("stop".equalsIgnoreCase(action)) {
			for (PlayerActionHandler playerActionHandler : AshleyServer.PLAYER_ACTION_HANDLERS) {
				if (playerActionHandler.player.getUuid() == playerMP.getUuid()) {
					playerActionHandler.stop();
				}
			}
		}
	}

	public List<String> getSuggestions(MinecraftServer server, CommandSource sender, String[] args, BlockPos pos) {
		if (args.length == 1) {
			return suggestMatching(args, "use", "attack", "stop");
		}
		if (args.length == 2) {
			return suggestMatching(args, "once", "continuous", "interval");
		}

		return Collections.emptyList();
	}

}
