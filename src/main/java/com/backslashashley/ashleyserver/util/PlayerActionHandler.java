package com.backslashashley.ashleyserver.util;

import com.backslashashley.ashleyserver.mixin.ICPacketPlayerDigging;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerHandActionC2SPacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;

import java.util.List;

public class PlayerActionHandler {
	public static PlayerHandActionC2SPacket createPlayerDiggingPacket(PlayerHandActionC2SPacket.Action action, Direction face, BlockPos pos) {
		PlayerHandActionC2SPacket packet = new PlayerHandActionC2SPacket();
		((ICPacketPlayerDigging) packet).setAction(action);
		((ICPacketPlayerDigging) packet).setFacing(face);
		((ICPacketPlayerDigging) packet).setPosition(pos);

		return packet;
	}

	public ServerPlayerEntity player;

	private boolean doesAttack;
	private int attackInterval;
	private int attackCooldown;

	private boolean doesUse;
	private int useInterval;
	private int useCooldown;

	private BlockPos currentBlock = new BlockPos(-1,-1,-1);
	private int blockHitDelay;
	private boolean isHittingBlock;
	private float curBlockDamageMP;

	public PlayerActionHandler(ServerPlayerEntity playerIn)
	{
		player = playerIn;
		stop();
	}

	public String toString() {
		return (doesAttack ? "t" : "f") + ":" +
			attackInterval + ":" +
			attackCooldown + ":" +
			(doesUse ? "t" : "f") + ":" +
			useInterval + ":" +
			useCooldown;
	}

	public PlayerActionHandler setAttack(int interval, int offset)
	{
		this.doesAttack = true;
		this.attackInterval = interval;
		this.attackCooldown = interval+offset;
		return this;
	}
	public PlayerActionHandler setUse(int interval, int offset)
	{
		this.doesUse = true;
		this.useInterval = interval;
		this.useCooldown = interval+offset;
		return this;
	}
	public PlayerActionHandler setUseForever()
	{
		this.doesUse = true;
		this.useInterval = 1;
		this.useCooldown = 1;
		return this;
	}
	public PlayerActionHandler setAttackForever()
	{
		this.doesAttack = true;
		this.attackInterval = 1;
		this.attackCooldown = 1;
		return this;
	}

	public PlayerActionHandler stop() {
		this.doesUse = false;
		this.doesAttack = false;
		resetBlockRemoving();
		return this;
	}

	public void onUpdate() {
		boolean used = false;

		if (doesUse && (--useCooldown)==0)
		{
			useCooldown = useInterval;
			used  = useOnce();
		}
		if (doesAttack)
		{
			if ((--attackCooldown) == 0)
			{
				attackCooldown = attackInterval;
				if (!(used)) attackOnce();
			}
			else
			{
				resetBlockRemoving();
			}
		}
	}

	public void attackOnce() {
		HitResult hitResult = mouseOver(player);
		if (hitResult == null) {
			return;
		}

		switch (hitResult.type)
		{
			case ENTITY:
				player.attack(hitResult.entity);
				this.player.swingHand(InteractionHand.MAIN_HAND);
				break;
			case MISS:
				break;
			case BLOCK:
				BlockPos blockpos = hitResult.getPos();
				if (player.getSourceWorld().getBlockState(blockpos).getMaterial() != Material.AIR)
				{
					onPlayerDamageBlock(blockpos, hitResult.face.getOpposite());
					this.player.swingHand(InteractionHand.MAIN_HAND);
					break;
				}
		}
	}

	public boolean useOnce() {
		HitResult hitResult = mouseOver(player);
		if (hitResult == null) {
			return false;
		}
		for (InteractionHand hand : InteractionHand.values())
		{
			ItemStack itemstack = this.player.getHandStack(hand);
			switch (hitResult.type) {
				case ENTITY:
					Entity target = hitResult.entity;
					Vec3d vec3d = new Vec3d(hitResult.face.getNormal().getX() - target.x, hitResult.face.getNormal().getY() - target.y, hitResult.face.getNormal().getZ() - target.z);

					boolean flag = player.canSee(target);
					double d0 = 36.0D;

					if (!flag) {
						d0 = 9.0D;
					}

					if (player.getSquaredDistanceTo(target) < d0) {
						InteractionResult res = player.interact(target, hand);
						if (res == InteractionResult.SUCCESS) {
							return true;
						}
						res = target.interact(player, vec3d, hand);
						if (res == InteractionResult.SUCCESS) {
							return true;
						}
					}
					break;
				case MISS:
					break;
				case BLOCK:
					BlockPos blockpos = hitResult.getPos();

					if (player.getSourceWorld().getBlockState(blockpos).getMaterial() != Material.AIR) {
						if (itemstack.isEmpty())
							continue;
						float x = (float) hitResult.offset.x;
						float y = (float) hitResult.offset.y;
						float z = (float) hitResult.offset.z;

						InteractionResult res = player.interactionManager.useBlock(player, player.getSourceWorld(), itemstack, hand, blockpos, hitResult.face, x, y, z);
						if (res == InteractionResult.SUCCESS) {
							this.player.swingHand(hand);
							return true;
						}
					}
			}
			InteractionResult res = player.interactionManager.useItem(player,player.getSourceWorld(),itemstack,hand);
			if (res == InteractionResult.SUCCESS)
			{
				return true;
			}
		}
		return false;
	}

	private static HitResult rayTraceBlocks(double blockReachDistance, ServerPlayerEntity player) {
		Vec3d eyeVec = player.getEyePosition(1.0F);
		Vec3d lookVec = player.getRotationVec(1.0F);
		Vec3d pointVec = eyeVec.add(lookVec.x * blockReachDistance, lookVec.y * blockReachDistance, lookVec.z * blockReachDistance);
		return player.getSourceWorld().rayTrace(eyeVec, pointVec, false, false, true);
	}

	public static HitResult mouseOver(ServerPlayerEntity player) {
		World world = player.getSourceWorld();
		if (world == null)
			return null;
		HitResult result = null;

		Entity pointedEntity = null;
		double reach = player.isCreative() ? 5.0D : 4.5D;
		result = rayTraceBlocks(reach, player);
		Vec3d eyeVec = 	player.getEyePosition(1.0F);
		boolean flag = !player.isCreative();
		if (player.isCreative()) reach = 6.0D;
		double extendedReach = reach;

		if (result != null)
		{
			extendedReach = result.offset.distanceTo(eyeVec);
			if (world.getBlockState(result.getPos()).getMaterial() == Material.AIR)
				result = null;
		}

		Vec3d lookVec = player.getRotationVec(1.0F);
		Vec3d pointVec = eyeVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		Vec3d hitVec = null;
		List<Entity> list = world.getEntities(
			player,
			player.getShape().expand(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach).grow(1.0D, 1.0D, 1.0D),
			Predicates.and(EntityFilter.NOT_SPECTATOR, entity -> entity != null && entity.hasCollision())
		);
		double d2 = extendedReach;

		for (int j = 0; j < list.size(); ++j)
		{
			Entity entity1 = list.get(j);
			Box box = entity1.getShape().expand((double) entity1.getExtraHitboxSize());
			HitResult raytraceresult = box.clip(eyeVec, pointVec);

			if (box.contains(eyeVec))
			{
				if (d2 >= 0.0D)
				{
					pointedEntity = entity1;
					hitVec = raytraceresult == null ? eyeVec : raytraceresult.offset;
					d2 = 0.0D;
				}
			}
			else if (raytraceresult != null)
			{
				double d3 = eyeVec.distanceTo(raytraceresult.offset);

				if (d3 < d2 || d2 == 0.0D)
				{
					if (entity1.getVehicle() == player.getVehicle())
					{
						if (d2 == 0.0D)
						{
							pointedEntity = entity1;
							hitVec = raytraceresult.offset;
						}
					}
					else
					{
						pointedEntity = entity1;
						hitVec = raytraceresult.offset;
						d2 = d3;
					}
				}
			}
		}

		if (pointedEntity != null && flag && eyeVec.distanceTo(hitVec) > 3.0D)
		{
			pointedEntity = null;
			result = new HitResult(HitResult.Type.MISS, hitVec, (Direction) null, new BlockPos(hitVec));
		}

		if (pointedEntity != null && (d2 < extendedReach || result == null))
		{
			result = new HitResult(pointedEntity, hitVec);
		}

		return result;
	}

	public boolean clickBlock(BlockPos loc, Direction face) {
		World world = player.getSourceWorld();
		if (player.interactionManager.getGameMode()!= GameMode.ADVENTURE)
		{
			if (player.interactionManager.getGameMode() == GameMode.SPECTATOR)
			{
				return false;
			}

			if (!player.abilities.canModifyWorld)
			{
				ItemStack itemstack = player.getMainHandStack();

				if (itemstack.isEmpty())
				{
					return false;
				}

				if (!itemstack.canEffectivelyMine(world.getBlockState(loc).getBlock().defaultState()))
				{
					return false;
				}
			}
		}

		if (!world.getWorldBorder().contains(loc))
		{
			return false;
		}
		else
		{
			if (player.interactionManager.getGameMode() == GameMode.CREATIVE)
			{
				player.networkHandler.handlePlayerHandAction(createPlayerDiggingPacket(PlayerHandActionC2SPacket.Action.START_DESTROY_BLOCK, face, loc));
				clickBlockCreative(world, loc, face);
				this.blockHitDelay = 5;
			}
			else if (!this.isHittingBlock || !(currentBlock.equals(loc)))
			{
				if (this.isHittingBlock)
				{
					player.networkHandler.handlePlayerHandAction(createPlayerDiggingPacket(PlayerHandActionC2SPacket.Action.ABORT_DESTROY_BLOCK, face, this.currentBlock));
				}

				BlockState iblockstate = world.getBlockState(loc);
				player.networkHandler.handlePlayerHandAction(createPlayerDiggingPacket(PlayerHandActionC2SPacket.Action.START_DESTROY_BLOCK, face, loc));
				boolean flag = iblockstate.getMaterial() != Material.AIR;

				if (flag && this.curBlockDamageMP == 0.0F)
				{
					iblockstate.getBlock().startMining(world, loc, player);
				}

				if (flag && iblockstate.getMiningSpeed(player, world, loc) >= 1.0F)
				{
					this.onPlayerDestroyBlock(loc);
				}
				else
				{
					this.isHittingBlock = true;
					this.currentBlock = loc;
					this.curBlockDamageMP = 0.0F;
					world.doGlobalEvent(player.getNetworkId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
				}
			}

			return true;
		}
	}

	private void clickBlockCreative(World world, BlockPos pos, Direction facing) {
		if (!world.extinguishFire(player, pos, facing))
		{
			onPlayerDestroyBlock(pos);
		}
	}

	public boolean onPlayerDamageBlock(BlockPos posBlock, Direction directionFacing) {
		if (this.blockHitDelay > 0)
		{
			--this.blockHitDelay;
			return true;
		}
		World world = player.getSourceWorld();
		if (player.interactionManager.getGameMode() == GameMode.CREATIVE && world.getWorldBorder().contains(posBlock))
		{
			this.blockHitDelay = 5;
			player.networkHandler.handlePlayerHandAction(createPlayerDiggingPacket(PlayerHandActionC2SPacket.Action.START_DESTROY_BLOCK, directionFacing, posBlock));
			clickBlockCreative(world, posBlock, directionFacing);
			return true;
		}
		else if (posBlock.equals(currentBlock))
		{
			BlockState iblockstate = world.getBlockState(posBlock);

			if (iblockstate.getMaterial() == Material.AIR)
			{
				this.isHittingBlock = false;
				return false;
			}
			else
			{
				this.curBlockDamageMP += iblockstate.getMiningSpeed(player, world, posBlock);

				if (this.curBlockDamageMP >= 1.0F)
				{
					this.isHittingBlock = false;
					player.networkHandler.handlePlayerHandAction(createPlayerDiggingPacket(PlayerHandActionC2SPacket.Action.STOP_DESTROY_BLOCK, directionFacing, posBlock));
					this.onPlayerDestroyBlock(posBlock);
					this.curBlockDamageMP = 0.0F;
					this.blockHitDelay = 5;
				}
				world.doGlobalEvent(-1, this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
				return true;
			}
		}
		else
		{
			return this.clickBlock(posBlock, directionFacing);
		}
	}

	private boolean onPlayerDestroyBlock(BlockPos pos) {
		World world = player.getSourceWorld();
		if (player.interactionManager.getGameMode() != GameMode.ADVENTURE)
		{
			if (player.interactionManager.getGameMode() == GameMode.SPECTATOR)
			{
				return false;
			}

			if (player.abilities.canModifyWorld)
			{
				ItemStack itemstack = player.getMainHandStack();

				if (itemstack.isEmpty())
				{
					return false;
				}

				if (!itemstack.canEffectivelyMine(world.getBlockState(pos).getBlock().defaultState()))
				{
					return false;
				}
			}
		}

		if (player.interactionManager.getGameMode()==GameMode.CREATIVE && !player.getMainHandStack().isEmpty() && player.getMainHandStack().getItem() instanceof SwordItem)
		{
			return false;
		}
		else
		{
			BlockState iblockstate = world.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if ((block instanceof CommandBlock || block instanceof StructureBlock) && !player.canUseMasterBlocks())
			{
				return false;
			}
			else if (iblockstate.getMaterial() == Material.AIR)
			{
				return false;
			}
			else
			{
				world.doEvent(2001, pos, Block.getId(iblockstate.getBlock()));
				block.beforeMinedByPlayer(world, pos, iblockstate, player);
				boolean flag = world.setBlockState(pos, Blocks.AIR.defaultState(), 11);

				if (flag)
				{
					block.onRemoved(world, pos, iblockstate);
				}

				this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());

				if (!(player.interactionManager.getGameMode()==GameMode.CREATIVE))
				{
					ItemStack itemstack1 = player.getMainHandStack();

					if (!itemstack1.isEmpty())
					{
						itemstack1.mineBlock(world, iblockstate, pos, player);

						if (itemstack1.isEmpty())
						{
							player.setHandStack(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
						}
					}
				}

				return flag;
			}
		}
	}

	public void resetBlockRemoving() {
		if (this.isHittingBlock)
		{
			player.networkHandler.handlePlayerHandAction(createPlayerDiggingPacket(PlayerHandActionC2SPacket.Action.ABORT_DESTROY_BLOCK, Direction.DOWN, this.currentBlock));
			this.isHittingBlock = false;
			this.curBlockDamageMP = 0.0F;
			player.getSourceWorld().updateBlockMiningProgress(player.getNetworkId(), this.currentBlock, -1);
			player.resetLastAttackedTicks();
			this.currentBlock = new BlockPos(-1,-1,-1);
		}
	}
}
