package com.backslashashley.ashleyserver.command;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class CommandFindBlock extends CommandBase {
	@Override
	public String getName() {
		return "findblock";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "findblock [block]";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (!(source instanceof ServerPlayerEntity)) {
			throw new CommandException("Unknown " + source.getName() + " tried to run /findblock!");
		}

		ServerPlayerEntity player = asPlayer(source);
		BlockPos playerPos = player.getSourceBlockPos();
		Block blockToFind = parseBlock(source, args[0]);

		WorldChunk chunk = player.world.getChunk(playerPos);
		ArrayList<BlockPos> targetBlocks = new ArrayList<>();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					BlockState state = chunk.getBlockState(new BlockPos(x,y,z));
					if (state.getBlock() == blockToFind) {
						targetBlocks.add(new BlockPos(x, y, z));
					}
				}
			}
		}
		if (targetBlocks.isEmpty()) {
			source.sendMessage(new LiteralText("No target blocks found in chunk (" + chunk.getPos().x + ", " + chunk.getPos().z + ")"));
		} else {
			source.sendMessage(new LiteralText(targetBlocks.size() + " target blocks found in chunk (" + chunk.getPos().x + ", " + chunk.getPos().z + ")"));
			for (BlockPos pos : targetBlocks) {
				source.sendMessage(new LiteralText("x" + (pos.getX() + (chunk.getPos().x * 16)) + " y" + pos.getY() + " z" + (pos.getZ() + (chunk.getPos().z * 16))));
			}
		}
	}

	@Override
	public List<String> getSuggestions(MinecraftServer server, CommandSource source, String[] args, BlockPos targetPos) {
		String[] fakeArgs = {"x", "y", "z", args[0]};
		return server.commandHandler.getCommands().get("setblock").getSuggestions(server, source, fakeArgs, targetPos);
	}
}
