package com.backslashashley.ashleyserver.util;

public interface CameraPlayer {
	boolean isCameraMode();
	void setCameraMode(boolean cameraMode);

	double getSurvivalX();
	double getSurvivalY();
	double getSurvivalZ();

	void setSurvivalX(double survivalX);
	void setSurvivalY(double survivalY);
	void setSurvivalZ(double survivalZ);

	float getSurvivalYaw();
	float getSurvivalPitch();

	void setSurvivalYaw(float survivalYaw);
	void setSurvivalPitch(float survivalPitch);
}
