package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.util.AFKPlayer;
import com.backslashashley.ashleyserver.stat.ModdedStats;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler {
	@Shadow
	public ServerPlayerEntity player;

	@Shadow
	private int ticks;

	@Inject(
		method = "tick",
		at = @At(
			value = "TAIL"
		)
	)
	private void trackTime(CallbackInfo ci) {
		AFKPlayer afkPlayer = (AFKPlayer) this.player;

		if (!afkPlayer.isAfk()) {
			int afkTime = 5 * 60 * 20; // 5 minutes in ticks
			if (afkPlayer.getTickLastAction() > 0L && this.ticks - afkPlayer.getTickLastAction() > afkTime) {
				afkPlayer.setAfk(true);
				this.player.incrementStat(ModdedStats.MINUTES_PLAYED_NO_AFK, -afkTime);
				this.player.incrementStat(ModdedStats.MINUTES_AFK, afkTime);
			} else {
				// Player is not AFK
				this.player.incrementStat(ModdedStats.MINUTES_PLAYED_NO_AFK, 1);
			}
		}
		else {
			this.player.incrementStat(ModdedStats.MINUTES_AFK, 1);
		}
	}

	@Inject(
		method = "handlePlayerMove",
		at = @At(
			value = "HEAD"
		)
	)
	private void updateLastActionTime(PlayerMoveC2SPacket packet, CallbackInfo ci) {
		if (packet instanceof PlayerMoveC2SPacket) {
			// TODO I think this if statement targets player doing mouse movement?
			if (packet.hasAngles) {
				((AFKPlayer) this.player).setTickLastAction(ticks);
				if (((AFKPlayer) this.player).isAfk()) {
					((AFKPlayer) this.player).setAfk(false);
				}
			}
		}
	}
}
