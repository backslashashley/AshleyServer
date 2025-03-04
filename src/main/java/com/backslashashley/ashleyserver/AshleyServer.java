package com.backslashashley.ashleyserver;

import com.backslashashley.ashleyserver.logging.LoggerRegistry;
import com.backslashashley.ashleyserver.stat.ScoreboardHandler;
import com.backslashashley.ashleyserver.util.AFKPlayer;
import com.backslashashley.ashleyserver.util.HUDController;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

import java.util.ArrayList;
import java.util.UUID;

public class AshleyServer implements ServerModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("Ashley Server");

	public static MinecraftServer server;
	public static final ArrayList<UUID> joinedPlayersSinceRestart = new ArrayList<>();

	@Override
	public void initServer() {
		LOGGER.info("Initializing Ashley Server");
		ScoreboardHandler.genShorthand();
		MinecraftServerEvents.START.register(AshleyServer::init);
		MinecraftServerEvents.TICK_START.register(AshleyServer::tick);
		MinecraftServerEvents.LOAD_WORLD.register(AshleyServer::onServerLoaded);
		MinecraftServerEvents.STOP.register(AshleyServer::stop);

		ServerConnectionEvents.LOGIN.register(AshleyServer::login);
	}

	private static void stop(MinecraftServer server) {
		AshleyServer.server = null;
	}

	public static void onServerLoaded(MinecraftServer server) {
		LoggerRegistry.initLoggers(server);
	}

	public static void login(MinecraftServer server, ServerPlayerEntity player) {
		LoggerRegistry.playerConnected(player);

		if (!joinedPlayersSinceRestart.contains(player.getUuid())) {
			joinedPlayersSinceRestart.add(player.getUuid());
			player.server.commandHandler.run(player.asEntity(), "/log tps quiet");
			player.server.commandHandler.run(player.asEntity(), "/log mobcaps quiet");
		}

		((AFKPlayer) player).setTickLastAction(0);
	}

	public static void init(MinecraftServer server) {
		AshleyServer.server = server;
	}

	public static void tick(MinecraftServer server) {
		HUDController.update_hud(server);
	}
}
