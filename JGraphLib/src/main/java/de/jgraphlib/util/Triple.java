package de.jgraphlib.util;

public class Triple<F, S, T> {
	private F first;
	private S second;
	private T third;

	public Triple() {}

	public Triple(F first, S second, T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public void setSecond(S second) {
		this.second = second;
	}

	public void setThird(T third) {
		this.third = third;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public T getThird() {
		return third;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("First: ").append(this.getFirst()).append(", Second: ")
				.append(this.getSecond()).append(", Third: ").append(this.getThird()).toString();
	}
}