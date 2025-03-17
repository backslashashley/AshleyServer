package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.AshleyServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/MinecraftServer;saveWorlds(Z)V"
		)
	)
	public void tick (CallbackInfo ci) {
		if (AshleyServer.logAutosave) {
			AshleyServer.server.sendMessage(new LiteralText("Autosave at: " + AshleyServer.server.getTicks()));
		}
	}
}
