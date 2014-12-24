package com.devmite.rubik.helper;

import java.util.List;

public class Utils {

	public static int[] listToIntArray(List<Integer> integers) {
		int[] ints = new int[integers.size()];
		int i = 0;
		for (Integer n : integers) {
			ints[i++] = n;
		}
		return ints;
	}
	
}
