package de.jgraphlib.util.treeparser;

public enum ValueType {
    INT {
	@Override
	public String toString() {
	    return "INT";
	};
    },
    DOUBLE {
	@Override
	public String toString() {
	    return "DOUBLE";
	};
    },
    STRING {
	@Override
	public String toString() {
	    return "STRING";
	};
    },
}
