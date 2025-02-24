package com.backslashashley.ashleyserver.mixin;

import net.minecraft.crafting.CraftingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

@Mixin(CraftingManager.class)
public class MixinCraftingManager {
	@Redirect(
		method = "load",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/stream/Stream;iterator()Ljava/util/Iterator;",
			remap = false
		)
	)
	private static Iterator<Path> fixRecipesIdDependenceOnFileSystemWalkOrder(final Stream<Path> instance) {
		return instance.sorted((a, b) -> b.toString().compareTo(a.toString())).iterator();
	}
}
