package de.jgraphlib.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomNumbers {

	private Random intRandom;
	private Random doubleRandom;

	public RandomNumbers(int seed) {
		intRandom = new Random(seed);
		doubleRandom = new Random(seed);
	}

	public RandomNumbers() {
		intRandom = new Random();
		doubleRandom = new Random();
	}

	public int getRandom(int min, int max) {
		if (min == max)
			return min;

		return intRandom.nextInt(max - min) + min;
	}

	public double getRandom(double min, double max) {
		if (min == max)
			return min;

		return min + (max - min) * doubleRandom.nextDouble();
	}

	public <E> List<E> selectNrandomOfM(List<E> list, int n, Random r) {

		int length = list.size();

		if (length <= n)
			return list;

		for (int i = length - 1; i >= length - n; --i)
			Collections.swap(list, i, intRandom.nextInt(i + 1));

		return list.subList(length - n, length);
	}

	public int getRandomNotInE(int min, int max, List<Integer> e) {
		int random = getRandom(min, max);
		
		while (e.contains(random))
			random = getRandom(min, max);
		
		return random;

	}
}
