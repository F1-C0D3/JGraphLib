package de.jgraphlib.util.treeparser;

public class Value extends Element{
	private ValueType type;
				
	public Value(ValueType type) {
		this.type = type;
	}
	
	public ValueType getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return this.type.toString();
	}
}
