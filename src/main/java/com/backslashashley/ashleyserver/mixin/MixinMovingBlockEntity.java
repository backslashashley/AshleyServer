package com.backslashashley.ashleyserver.mixin;

import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MovingBlockEntity;
import net.minecraft.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovingBlockEntity.class)
public class MixinMovingBlockEntity extends BlockEntity {
	@Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/BlockState;I)Z"))
	private void tick(CallbackInfo ci) {
		final BlockState state = this.world.getBlockState(this.pos);
		this.world.onBlockChanged(pos.offset(state.get(PistonHeadBlock.FACING).getOpposite()), state.getBlock(), false);
	}
}
