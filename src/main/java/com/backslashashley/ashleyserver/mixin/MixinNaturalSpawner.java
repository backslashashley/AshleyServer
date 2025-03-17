package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.util.SpawnReporter;
import net.minecraft.entity.living.mob.MobCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.NaturalSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NaturalSpawner.class)
public class MixinNaturalSpawner {
	private static final MobCategory[] OVERWORLD = new MobCategory[]{MobCategory.MONSTER, MobCategory.CREATURE, MobCategory.WATER_CREATURE, MobCategory.AMBIENT};
	private static final MobCategory[] OTHERWORLD = new MobCategory[]{MobCategory.MONSTER};
	@Shadow
	@Final
	private static int MOB_CAPACITY_CHUNK_AREA;

	@Inject(
		method = "tick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/NaturalSpawner;MOB_CAPACITY_CHUNK_AREA:I",
			shift = At.Shift.AFTER
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void getMobcaps(ServerWorld server, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate, CallbackInfoReturnable<Integer> cir, int chunkAddsToMobcap, int mobTypeSpawned, BlockPos spawnPoint, MobCategory[] mobCategories, int idk, int wtf, MobCategory mobCategory, int loadedOfMobCategory) {
		final int mobCap = mobCategory.getCap() * chunkAddsToMobcap / MOB_CAPACITY_CHUNK_AREA;
		SpawnReporter.mobcaps.get(server.dimension.getType().getId()).put(mobCategory, new Pair<>(loadedOfMobCategory, mobCap));
	}

	@Redirect(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/mob/MobCategory;values()[Lnet/minecraft/entity/living/mob/MobCategory;"
		)
	)
	private MobCategory[] preventUselessMobSpawningAttemptsInIncorrectDimensions(ServerWorld world) {
		return world.dimension.isOverworld() ? OVERWORLD : OTHERWORLD;
	}
}
