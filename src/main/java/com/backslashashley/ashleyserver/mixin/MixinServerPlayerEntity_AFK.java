package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.util.AFKPlayer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity_AFK implements AFKPlayer {
	@Unique private boolean isAfk;
	@Unique private int tickLastAction;
	@Unique private float lastPitch;
	@Unique private float lastYaw;

	@Override
	public boolean isAfk() {
		return this.isAfk;
	}

	@Override
	public void setAfk(boolean isAfk) {
		this.isAfk = isAfk;
	}

	@Override
	public int getTickLastAction() {
		return this.tickLastAction;
	}

	@Override
	public void setTickLastAction(int time) {
		this.tickLastAction = time;
	}

	@Override
	public void setLastPitch(float pitch) {
		this.lastPitch = pitch;
	}

	@Override
	public void setLastYaw(float yaw) {
		this.lastYaw = yaw;
	}

	@Override
	public float getLastPitch() {
		return lastPitch;
	}

	@Override
	public float getLastYaw() {
		return lastYaw;
	}


	@Inject(
		method = "copyFrom",
		at = @At(
			value = "TAIL"
		)
	)
	private void copyData(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		this.isAfk = ((AFKPlayer) oldPlayer).isAfk();
		this.tickLastAction = ((AFKPlayer) oldPlayer).getTickLastAction();
	}
}

