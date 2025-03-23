package com.backslashashley.ashleyserver.mixin;

import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity {
	public MixinMobEntity(World world) {
		super(world);
	}

	@Inject(
		method = "playAmbientSound",
		at = @At(
			value = "HEAD"
		),
		cancellable = true
	)
	public void playAmbientSound(CallbackInfo ci) {
		String customName = this.getCustomName();
		if (customName.equalsIgnoreCase("he sweep") ||
			customName.equalsIgnoreCase("he attack") ||
			customName.equalsIgnoreCase("stfu")) {
			ci.cancel();
		}
	}
}
