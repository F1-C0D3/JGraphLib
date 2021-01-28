package de.jgraphlib.util.treeparser;

public class Requirement extends Element {

	private final Boolean isRequired;
	
	public Requirement() {
		this.isRequired = true;
	} 
	
	public Requirement(boolean isRequired) {
		this.isRequired = isRequired;
	}	
	
	@Override
	public String toString() {		
		return this.isRequired.toString();		
	}
	
	public Boolean isOptional(){
		return !this.isRequired;
	}
	
	public Boolean isRequired() {
		return this.isRequired;
	}
}
