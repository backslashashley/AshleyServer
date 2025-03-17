package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.handler.CommandListener;
import net.minecraft.server.command.handler.CommandManager;
import net.minecraft.server.command.handler.CommandRegistry;
import net.minecraft.server.command.source.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager extends CommandRegistry implements CommandListener {
	@Inject(
		method = "<init>",
		at = @At(
			value = "RETURN"
		)
	)
	private void onCtor(MinecraftServer server, CallbackInfo ci) {
		this.register(new CommandPing());
		this.register(new CommandStackBoxes());
		this.register(new CommandLog());
		this.register(new CommandFindBlock());
		this.register(new CommandUpTime());
		this.register(new CommandCamera());
		this.register(new CommandStat());
		this.register(new CommandLogAutosave());
		this.register(new CommandMacro());
		this.register(new CommandBot());
	}

	@Inject(
		method = "sendSuccess",
		at = @At(
			value = "HEAD"
		),
		cancellable = true
	)
	private void silenceRcon(CommandSource source, Command command, int flags, String message, Object[] args, CallbackInfo ci) {
		if (source.getName().equals("Rcon")) {
			ci.cancel();
		}
	}
}
