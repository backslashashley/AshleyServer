package com.backslashashley.ashleyserver.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.particle.ParticleType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	public MixinLivingEntity(World world) {
		super(world);
	}

	@Redirect(
		method = "renderBrokenItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V")
	)
	private void fixToolBreakSound(LivingEntity livingEntity, SoundEvent soundIn, float volume, float pitch) {
		this.world.playSound(null, this.x, this.y, this.z, soundIn, this.getSoundCategory(), volume, pitch);
	}

	@Redirect(
		method = "renderBrokenItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/entity/particle/ParticleType;DDDDDD[I)V")
	)
	private void fixToolParticle(World instance, ParticleType type, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int[] parameters) {
		((ServerWorld) world).addParticle(type, x, y, z, 0, x, y + 0.05D, z, 0.0D, parameters[0], new ItemStack(Item.byId(parameters[0])).getMetadata());
	}
}
