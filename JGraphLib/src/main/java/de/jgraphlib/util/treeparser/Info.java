package de.jgraphlib.util.treeparser;

public class Info extends Element {
	public final String string;
	
	public Info(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return this.string;
	}	
}