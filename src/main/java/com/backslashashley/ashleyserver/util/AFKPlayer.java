package com.backslashashley.ashleyserver.util;

public interface AFKPlayer {
	boolean isAfk();
	void setAfk(boolean isAfk);
	int getTickLastAction();
	void setTickLastAction(int time);
	void setLastPitch(float pitch);
	void setLastYaw(float yaw);
	float getLastPitch();
	float getLastYaw();
}
