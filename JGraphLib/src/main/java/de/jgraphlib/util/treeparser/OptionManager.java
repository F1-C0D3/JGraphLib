package de.jgraphlib.util.treeparser;

import java.util.ArrayList;

public class OptionManager {
	private ArrayList<Option> option;
    private ArrayList<KeyOption> keyOptions;
    private ArrayList<ValueOption> valueOptions;

    private AddOptionVisitor addOptionsVisitor = new AddOptionVisitor(this);

    public OptionManager() {
    	this.option = new ArrayList<>();
    	this.keyOptions = new ArrayList<>();
    	this.valueOptions = new ArrayList<>();
    }
    
    public void add(Option option) {
    	option.accept(addOptionsVisitor);
    }
    
    public void add(KeyOption keyOption) {
    	keyOptions.add(keyOption);
    }
    
    public void add(ValueOption valueOption) {
    	valueOptions.add(valueOption);
    }    
    
    public boolean isKeyOption(KeyOption keyOption) {
    	return true;
    }
    
    public boolean isKeyOption(ValueOption valueOption) {
    	return false;
    }
    
    ArrayList<KeyOption> getKeyOptions() {
    	return this.keyOptions;
    }
    
    public ArrayList<ValueOption> getValueOptions() {
    	return this.valueOptions;
    }
}
