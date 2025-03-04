package com.backslashashley.ashleyserver.mixin;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.FireworksItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworksItem.class)
public class MixinFireworksItem {
	@Inject(method = "startUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addEntity(Lnet/minecraft/entity/Entity;)Z"))
	public void startUsing (World world, PlayerEntity player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
		player.incrementStat(Stats.itemUsed(player.getHandStack(hand).getItem()));
	}
}
