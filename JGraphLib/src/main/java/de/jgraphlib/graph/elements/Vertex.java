package de.jgraphlib.graph.elements;

public class Vertex<P> {

	int ID;
	P position;

	public Vertex() {
	}

	public Vertex(P position) {
		this.position = position;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return this.ID;
	}

	public void setPosition(P position) {
		this.position = position;
	}

	public P getPosition() {
		return this.position;
	}

	public boolean equals(Vertex<P> vertex) {
		return vertex.getID() == this.ID;
	}
	
	public String toString() {
		return String.format("%d: %s", ID, position.toString());
	}
}
