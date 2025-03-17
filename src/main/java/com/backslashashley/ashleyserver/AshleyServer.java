package com.backslashashley.ashleyserver;

import com.backslashashley.ashleyserver.logging.LoggerRegistry;
import com.backslashashley.ashleyserver.stat.ScoreboardHandler;
import com.backslashashley.ashleyserver.util.AFKPlayer;
import com.backslashashley.ashleyserver.util.PlayerActionHandler;
import com.backslashashley.ashleyserver.util.HUDController;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.UUID;

public class AshleyServer implements ServerModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("Ashley Server");

	public static boolean logAutosave = false;

	public static MinecraftServer server;
	public static final ArrayList<UUID> joinedPlayersSinceRestart = new ArrayList<>();
	public static final ArrayList<PlayerActionHandler> PLAYER_ACTION_HANDLERS = new ArrayList<>();

	@Override
	public void initServer() {
		LOGGER.info("Initializing Ashley Server");
		ScoreboardHandler.genShorthand();
		MinecraftServerEvents.START.register(AshleyServer::init);
		MinecraftServerEvents.TICK_START.register(AshleyServer::tick);
		MinecraftServerEvents.LOAD_WORLD.register(AshleyServer::onServerLoaded);
		MinecraftServerEvents.STOP.register(AshleyServer::stop);

		ServerConnectionEvents.LOGIN.register(AshleyServer::login);
		ServerConnectionEvents.DISCONNECT.register(AshleyServer::disconnect);
	}

	private static void stop(MinecraftServer server) {
		AshleyServer.server = null;
	}

	private static void onServerLoaded(MinecraftServer server) {
		LoggerRegistry.initLoggers(server);
	}

	private static void login(MinecraftServer server, ServerPlayerEntity player) {
		LoggerRegistry.playerConnected(player);

		if (!joinedPlayersSinceRestart.contains(player.getUuid())) {
			joinedPlayersSinceRestart.add(player.getUuid());
			player.server.commandHandler.run(player.asEntity(), "/log tps quiet");
			player.server.commandHandler.run(player.asEntity(), "/log mobcaps quiet");
		}

		PLAYER_ACTION_HANDLERS.add(new PlayerActionHandler(player));

		((AFKPlayer) player).setTickLastAction(0);
	}

	private static void disconnect(MinecraftServer server, ServerPlayerEntity player) {
		for (PlayerActionHandler playerActionHandler : PLAYER_ACTION_HANDLERS) {
			if (playerActionHandler.player.getUuid() == player.getUuid()) {
				PLAYER_ACTION_HANDLERS.remove(playerActionHandler);
			}
		}
	}

	public static void init(MinecraftServer server) {
		AshleyServer.server = server;
	}

	public static void tick(MinecraftServer server) {
		HUDController.update_hud(server);

		for (PlayerActionHandler playerActionHandler : PLAYER_ACTION_HANDLERS) {
			playerActionHandler.onUpdate();
		}
	}
}
