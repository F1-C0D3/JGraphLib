package de.jgraphlib.generator;


public class PathProblemProperties {

	private  int pathCount;
	private  int minLength;
	private  int maxLength;
	
	public PathProblemProperties(int pathCount, int minLength, int maxLength) {
		
		this.pathCount = pathCount;
		this.minLength=minLength;
		this.maxLength = maxLength;
	}

	public int getPathCount() {
		return pathCount;
	}

	public void setPathCount(int pathCount) {
		this.pathCount = pathCount;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	
	
}
