package de.jgraphlib.graph.generator;

public class ClusterGraphProperties extends GraphProperties {

	private int clusterQuantity;
	private DoubleRange clusterEdgeDistance;	
	
	public ClusterGraphProperties(
			int width, 
			int height, 
			IntRange vertexCount, 
			DoubleRange vertexDistance,
			IntRange edgeCount, 
			DoubleRange edgeDistance, 
			int clusterQuantity,
			DoubleRange clusterEdgeDistance) {
		
		super(width, height, vertexCount, vertexDistance, null, edgeDistance);	
		this.clusterQuantity = clusterQuantity;
		this.clusterEdgeDistance = clusterEdgeDistance;
	}
	
	public int getClusterQuantity() {
		return clusterQuantity;
	}
	
	public DoubleRange getClusterEdgeDistance() {
		return clusterEdgeDistance;
	}	
}
