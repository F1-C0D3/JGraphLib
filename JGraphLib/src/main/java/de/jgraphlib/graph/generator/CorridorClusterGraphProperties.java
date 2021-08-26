package de.jgraphlib.graph.generator;

public class CorridorClusterGraphProperties extends GraphProperties{

	int corridorQuantity;
	int corridorDistance;
	CorridorDirection corridorDirection; 
	
	public CorridorClusterGraphProperties(
			int width, 
			int height, 
			IntRange vertexCount, 
			DoubleRange vertexDistance,
			DoubleRange edgeDistance,
			int corridorQuantity,
			int corridorDistance,
			CorridorDirection corridorDirection) {
		
		super(width, height, vertexCount, vertexDistance, null, edgeDistance);
		this.corridorQuantity = corridorQuantity;
		this.corridorDistance = corridorDistance;
		this.corridorDirection = corridorDirection;
	}
	
	public enum CorridorDirection {
		HORIZONTAL, VERTICAL
	}		
	
	public int getCorridorQuantity() {
		return corridorQuantity;
	}
	
	public int getCorridorDistance() {
		return corridorDistance;
	}
	
	public CorridorDirection getCorridorDirection() {
		return corridorDirection;
	}
}
