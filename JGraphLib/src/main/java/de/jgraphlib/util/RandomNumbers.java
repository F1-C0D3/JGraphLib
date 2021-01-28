package de.jgraphlib.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomNumbers {

	public static int getRandom(int min, int max) {
		if (min == max)
			return min;

		Random random = new Random();

		return random.nextInt(max - min) + min;
	}

	public static double getRandom(double min, double max) {
		if (min == max)
			return min;

		Random random = new Random();

		return min + (max - min) * random.nextDouble();
	}

	public static <E> List<E> selectNrandomOfM(List<E> list, int n, Random r) {

		int length = list.size();

		if (length <= n)
			return list;

		for (int i = length - 1; i >= length - n; --i)
			Collections.swap(list, i, r.nextInt(i + 1));

		return list.subList(length - n, length);
	}
}
