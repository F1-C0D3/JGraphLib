package de.jgraphlib.util.treeparser;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Option
{
	protected Info info;
	protected Function function;
	private ArrayList<Option> options;
	protected Requirement requirement;		
	
	public Option() {
		this.requirement = new Requirement(false);
		this.options = new ArrayList<Option>();
	}
	
	public void accept(OptionVisitor optionVisitor) {
		optionVisitor.visit(this);
	}

	public void add(Option option) {
		options.add(option);
	}
	
	public Boolean hasOptions() {
		return this.options != null;
	}
		
	public ArrayList<Option> getOptions() {
		return this.options;
	}
	
	public ArrayList<KeyOption> getKeyOptions(){
        OptionManager optionManager = new OptionManager();        
		for(Option option : this.options) 
			optionManager.add(option);
		return optionManager.getKeyOptions();
	}
		
	public ArrayList<ValueOption> getValueOptions(){
        OptionManager optionManager = new OptionManager();        
		for(Option option : this.options) 
			optionManager.add(option);
		return optionManager.getValueOptions();
	}

	public abstract Key getKey();
	
	public Info getInfo() {
		return this.info;
	}
	
	public Function getFunction() {
		return this.function;
	}
	
	public Requirement getRequirement() {
		return this.requirement;
	}
	
	public Boolean isOptional() {
		return this.requirement.isOptional();
	} 
	
	public Boolean requiresOption() {		
		for(Option option : this.options)
        	if(!option.isOptional()) return true;      	    
		return false;		
	}
		
	@Override
	public String toString() {
		 StringBuilder stringBuilder = new StringBuilder();
	     this.print(stringBuilder, "", "");
	     return stringBuilder.toString();
	}
	
	protected String buildString() {
		return "";
	}
	
	private void print(StringBuilder stringBuilder, String prefix, String childrenPrefix) {
		stringBuilder.append(prefix);
		stringBuilder.append(this.buildString());
		stringBuilder.append('\n');  
        Iterator<Option> iterator = options.iterator();    
        while (iterator.hasNext()) {
        	Option next = iterator.next();   
        	if (iterator.hasNext()) 
                next.print(stringBuilder, childrenPrefix + "????????? ", childrenPrefix + "???   ");
            else 
                next.print(stringBuilder, childrenPrefix + "????????? ", childrenPrefix + "    ");       
        }
    }
}