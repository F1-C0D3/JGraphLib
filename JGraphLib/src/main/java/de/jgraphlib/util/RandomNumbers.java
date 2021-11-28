package de.jgraphlib.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomNumbers {

	long seed;
	private static Random intRandom;
	private static Random doubleRandom;
	private static RandomNumbers randomNumbers;

	public RandomNumbers(long seed) {
		this.seed = seed;
		intRandom = new Random(seed);
		doubleRandom = new Random(seed);
	}
	
	public Random getDoubleRandom() {
		return doubleRandom;
	}

	public RandomNumbers() {
		//this.seed = System.nanoTime();
		this.seed = new Random().nextLong();
		intRandom = new Random(seed);		
		doubleRandom = new Random(seed);
	}
	
	public long getSeed() {
		return seed;
	}

	final static public RandomNumbers getInstance(int seed) {
		if (doubleRandom == null && intRandom == null) {
			if (seed == -1) {
				randomNumbers = new RandomNumbers();
			} else {
				randomNumbers = new RandomNumbers(seed);
			}
		}

		return randomNumbers;
	}

	public int getRandom(int min, int max) {
				
		if (min >= max)
			return min;

		return intRandom.nextInt(max - min) + min;
	}

	public double getRandom(double min, double max) {
		if (min == max)
			return min;

		return min + (max - min) * doubleRandom.nextDouble();
	}

	public <E> List<E> selectNrandomOfM(List<E> list, int n) {

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
	
	public List<Integer> selectNranndomInInterval(int n, int min, int max){
		
		List<Integer> numbers = new ArrayList<Integer>();
		
		for(int i=0; i<n; i++)
			numbers.add(getRandom(min,max));
		
		return numbers;	
	}
}
