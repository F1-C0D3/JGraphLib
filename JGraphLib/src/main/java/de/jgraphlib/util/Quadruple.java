package de.jgraphlib.util;

public class Quadruple<F, S, T, H> {
	private F first;
	private S second;
	private T third;
	private H fourth;

	public Quadruple() {
	}

	public Quadruple(F first, S second, T third, H fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
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

	public H getFourth() {
		return fourth;
	}

	public void setFourth(H fourth) {
		this.fourth = fourth;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("First: ").append(this.getFirst()).append(", Second: ")
				.append(this.getSecond()).append(", Third: ").append(this.getThird()).append(",Fourth: ")
				.append(this.getFourth()).toString();
	}
}