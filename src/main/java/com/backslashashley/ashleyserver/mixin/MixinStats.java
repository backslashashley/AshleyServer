package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.stat.ModdedStats;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stats.class)
public class MixinStats {
	@Inject(method = "init", at = @At("TAIL"))
	private static void registerExtraStats(CallbackInfo ci) {
		ModdedStats.init();
	}
}

