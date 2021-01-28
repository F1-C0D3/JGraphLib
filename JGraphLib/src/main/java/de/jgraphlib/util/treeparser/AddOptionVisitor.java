package de.jgraphlib.util.treeparser;

public class AddOptionVisitor implements OptionVisitor {
    private OptionManager optionManager;

    public AddOptionVisitor(OptionManager optionManager) {
        this.optionManager = optionManager;
    }
    	
    @Override
    public void visit(Option option) {
    	this.optionManager.add(option);
    }
    
    @Override
    public void visit(KeyOption keyOption) {
    	this.optionManager.add(keyOption);
    }

    @Override
    public void visit(ValueOption valueOption) {
    	this.optionManager.add(valueOption);
    }
}
