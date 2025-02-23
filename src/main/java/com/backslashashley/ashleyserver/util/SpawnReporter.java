package com.backslashashley.ashleyserver.util;

import net.minecraft.entity.living.mob.MobCategory;
import net.minecraft.util.Pair;

import java.util.HashMap;

public class SpawnReporter {
	public static final HashMap<Integer, HashMap<MobCategory, Pair<Integer, Integer>>> mobcaps = new HashMap<>();

	static {
		mobcaps.put(-1, new HashMap<>());
		mobcaps.put(0, new HashMap<>());
		mobcaps.put(1, new HashMap<>());
	}
}
