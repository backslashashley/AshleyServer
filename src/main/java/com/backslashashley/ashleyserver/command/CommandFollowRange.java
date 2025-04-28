package com.backslashashley.ashleyserver.command;

import com.backslashashley.ashleyserver.util.PlayerActionHandler;
import net.minecraft.entity.living.attribute.EntityAttributes;
import net.minecraft.entity.living.mob.hostile.ZombiePigmanEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HitResult;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class CommandFollowRange extends CommandBase {
	@Override
	public String getName() {
		return "followRange";
	}

	@Override
	public String getUsage(CommandSource source)
	{
		return "Usage: followRange <entity_selector>";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (parseEntity(server, source, args[0]) instanceof ZombiePigmanEntity) {
			double followRange = ((ZombiePigmanEntity) parseEntity(server, source, args[0])).getAttribute(EntityAttributes.FOLLOW_RANGE).get();
			DecimalFormat df = new DecimalFormat("#.##");
			source.sendMessage(new LiteralText("Follow range: " + df.format(followRange)));
		}
	}

	@Override
	public List<String> getSuggestions(MinecraftServer server, CommandSource source, String[] args, @Nullable BlockPos pos) {
		List<String> list = suggestMatching(args, server.getPlayerNames());
		HitResult result = PlayerActionHandler.mouseOver((ServerPlayerEntity) source);
		if (result != null && result.type == HitResult.Type.ENTITY) {
			list.add(result.entity.getUuid().toString());
		}
		return args.length == 1 ? list : Collections.emptyList();
	}
}
