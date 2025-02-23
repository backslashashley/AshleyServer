package com.backslashashley.ashleyserver.command;

import com.backslashashley.ashleyserver.mixin.IShulkerBoxBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.slot.InventorySlot;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShulkerBoxItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class CommandStackBoxes extends CommandBase{
	@Override
	public String getName() {
		return "stackboxes";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "stackboxes [stacksize]";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (!(source instanceof ServerPlayerEntity)) {
			throw new CommandException("Unknown " + source.getName() + " tried to run /stackboxes!");
		}

		int stackSize = 16;
		if (args.length > 0) {
			stackSize = Integer.parseInt(args[0]);
			if (stackSize > 16 || stackSize < 1) {
				throw new CommandException("Stack size must be between 1-16");
			}
		}

		Map<DyeColor, Integer> boxesToStack = new HashMap<>();
		final ServerPlayerEntity player = asPlayer(source);
		for (final InventorySlot slot : player.playerMenu.slots) {
			final Pair<DyeColor, Integer> pair = getShulkerBoxColourAndCount(slot.getStack());
			if (pair.getRight() > 0) {
				boxesToStack.merge(pair.getLeft(), pair.getRight(), Integer::sum);
				slot.setStack(ItemStack.EMPTY);
			}
		}

		for (Map.Entry<DyeColor, Integer> entry : boxesToStack.entrySet()) {
			if (entry.getValue() > 0) {
				int stackCount = 0;
				for (int i = 0; i < entry.getValue(); i++) {
					stackCount++;
					if (stackCount == stackSize) {
						final ItemStack stack = new ItemStack(ShulkerBoxBlock.byColor(entry.getKey()), stackSize);
						if (!player.addItem(stack)) {
							player.dropItem(stack, false);
						}
						stackCount = 0;
					}
				}
				final ItemStack stack = new ItemStack(ShulkerBoxBlock.byColor(entry.getKey()), stackCount);
				if (!player.addItem(stack)) {
					player.dropItem(stack, false);
				}
			}
		}

		player.playerMenu.updateListeners();
	}

	private Pair<DyeColor, Integer> getShulkerBoxColourAndCount(final ItemStack stack) {
		if (stack.getItem() instanceof ShulkerBoxItem) {
			NbtCompound cmp = this.getCompoundOrNull(stack);
			if (cmp == null || cmp.getList("Items", 10).isEmpty()) {
				final DyeColor dye = ((IShulkerBoxBlock) ((ShulkerBoxItem) stack.getItem()).getBlock()).getColor();
				return new ImmutablePair<>(dye, stack.getSize());
			}
		}

		return new ImmutablePair<>(DyeColor.WHITE, 0);
	}

	private NbtCompound getCompoundOrNull(final ItemStack stack) {
		final NbtCompound compound = stack.getNbt();
		if (compound != null && compound.contains("BlockEntityTag")) {
			return compound.getCompound("BlockEntityTag");
		} else {
			return null;
		}
	}
}
