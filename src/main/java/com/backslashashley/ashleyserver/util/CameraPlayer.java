package com.backslashashley.ashleyserver.util;

public interface CameraPlayer {
	boolean isCameraMode();
	void setCameraMode(boolean cameraMode);

	double getSurvivalX();
	double getSurvivalY();
	double getSurvivalZ();

	void setSurvivalX(double x);
	void setSurvivalY(double Y);
	void setSurvivalZ(double Z);
}
