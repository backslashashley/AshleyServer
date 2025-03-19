package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.util.CameraPlayer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity_Camera implements CameraPlayer {
	@Unique private boolean isCameraMode;
	@Unique private double survivalX;
	@Unique private double survivalY;
	@Unique private double survivalZ;
	@Unique private float survivalYaw;
	@Unique private float survivalPitch;

	@Override
	public boolean isCameraMode() {
		return this.isCameraMode;
	}

	@Override
	public void setCameraMode(boolean cameraMode) {
		this.isCameraMode = cameraMode;
	}

	@Override
	public double getSurvivalX() {
		return this.survivalX;
	}
	@Override
	public double getSurvivalY() {
		return this.survivalY;
	}
	@Override
	public double getSurvivalZ() {
		return this.survivalZ;
	}

	@Override
	public void setSurvivalX(double survivalX) {
		this.survivalX = survivalX;
	}
	@Override
	public void setSurvivalY(double survivalY) {
		this.survivalY = survivalY;
	}
	@Override
	public void setSurvivalZ(double survivalZ) {
		this.survivalZ = survivalZ;
	}

	@Override
	public float getSurvivalYaw() {
		return this.survivalYaw;
	}
	@Override
	public float getSurvivalPitch() {
		return this.survivalPitch;
	}

	@Override
	public void setSurvivalYaw(float survivalYaw) {
		this.survivalYaw = survivalYaw;
	}
	@Override
	public void setSurvivalPitch(float survivalPitch) {
		this.survivalPitch = survivalPitch;
	}
}
