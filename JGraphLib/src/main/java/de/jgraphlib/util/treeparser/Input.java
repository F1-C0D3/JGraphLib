package de.jgraphlib.util.treeparser;

import java.util.ArrayList;
import java.util.List;

public class Input {

    public List<Integer> INT;
    public List<Double> DOUBLE;
    public List<String> STRING;

    public Input() {
	this.INT = new ArrayList<Integer>();
	this.DOUBLE = new ArrayList<Double>();
	this.STRING = new ArrayList<String>();
   }
}
