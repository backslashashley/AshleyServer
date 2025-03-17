package com.backslashashley.ashleyserver.bots;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketFlow;

public class BotConnection extends Connection {
	BotConnection() {
		super(PacketFlow.CLIENTBOUND);
	}

	@Override
	public void disableAutoRead() {
	}
}
