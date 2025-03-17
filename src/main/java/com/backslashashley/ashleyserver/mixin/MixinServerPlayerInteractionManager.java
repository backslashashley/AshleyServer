package com.backslashashley.ashleyserver.mixin;

import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.ServerPlayerInteractionManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
	@Shadow public ServerPlayerEntity player;
	@Shadow public World world;

	@Inject(
		method = "startMiningBlock",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;updateBlockMiningProgress(ILnet/minecraft/util/math/BlockPos;I)V"
		)
	)
	private void notifyUpdate(BlockPos pos, Direction face, CallbackInfo ci) {
		this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(this.world, pos));
	}
}
