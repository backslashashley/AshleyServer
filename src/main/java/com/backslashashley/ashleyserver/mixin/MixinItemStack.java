package com.backslashashley.ashleyserver.mixin;

import com.backslashashley.ashleyserver.stat.ModdedStats;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MixinItemStack {
	@Inject(
		method = "mineBlock",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"
		)
	)
	public void digStat(World world, BlockState state, BlockPos pos, PlayerEntity player, CallbackInfo ci) {
		if (player.getMainHandStack().getItem() instanceof PickaxeItem ||
			player.getMainHandStack().getItem() instanceof ShovelItem ||
			player.getMainHandStack().getItem() instanceof AxeItem) {
			player.incrementStat(ModdedStats.TOTAL_DIGS);
		}
	}
}
