package com.backslashashley.ashleyserver.stat;

import net.minecraft.stat.GeneralStat;
import net.minecraft.stat.Stat;
import net.minecraft.text.TranslatableText;

public class ModdedStats {
	public static Stat MINUTES_PLAYED_NO_AFK = new GeneralStat("stat.playOneMinuteNoAFK", new TranslatableText("stat.playOneMinuteNoAFK"), Stat.TIME_FORMATTER).setLocal().register();
	public static Stat MINUTES_AFK = new GeneralStat("stat.minutesAFK", new TranslatableText("stat.minutesAFK"), Stat.TIME_FORMATTER).setLocal().register();
	public static Stat TOTAL_DIGS = new GeneralStat("stat.totalDigs", new TranslatableText("stat.totalDigs")).register();

	public static void init() {
	}
}

