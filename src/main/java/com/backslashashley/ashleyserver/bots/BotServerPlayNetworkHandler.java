package com.backslashashley.ashleyserver.bots;

import net.minecraft.network.Connection;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

public class BotServerPlayNetworkHandler extends ServerPlayNetworkHandler {
	public BotServerPlayNetworkHandler(MinecraftServer server, Connection connection, ServerPlayerEntity player) {
		super(server, connection, player);
	}

	@Override
	public void sendPacket(final Packet<?> packet) {
	}

	@Override
	public void sendDisconnect(final Text reason) {
		this.player.discard();
	}
}



