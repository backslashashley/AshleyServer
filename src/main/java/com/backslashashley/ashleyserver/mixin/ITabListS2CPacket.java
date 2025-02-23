package com.backslashashley.ashleyserver.mixin;

import net.minecraft.network.packet.s2c.play.TabListS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TabListS2CPacket.class)
public interface ITabListS2CPacket {
	@Accessor("header")
	void setHeader(Text header);

	@Accessor("footer")
	void setFooter(Text footer);
}
