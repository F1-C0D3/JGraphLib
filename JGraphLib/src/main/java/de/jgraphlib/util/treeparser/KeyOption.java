package de.jgraphlib.util.treeparser;

public class KeyOption extends Option {
    private Key flag;
    private Key key;

    public KeyOption(String flag, Key command, Info info, Function function, Requirement requirement,
	    KeyOption keyOption) {
	this.flag = new Key(flag);
	this.key = command;
	super.info = info;
	super.function = function;
	super.requirement = requirement;
    }

    public KeyOption(String flag, Key command, Info info, Function function, Requirement requirement) {
	this.flag = new Key(flag);
	this.key = command;
	super.info = info;
	super.function = function;
	super.requirement = requirement;
    }

    public KeyOption(String flag, Key command, Info info, Function function) {
	this.flag = new Key(flag);
	this.key = command;
	super.info = info;
	super.function = function;
    }

    public KeyOption(Key command, Info info, Function function, Requirement requirement) {
	this.flag = new Key("");
	this.key = command;
	super.info = info;
	super.function = function;
	super.requirement = requirement;
    }

    public KeyOption(Key command, Info info, Function function) {
	this.flag = new Key("");
	this.key = command;
	super.info = info;
	super.function = function;
    }

    public KeyOption(Key command, Info info, Requirement requirement) {
	this.flag = new Key("");
	this.key = command;
	super.info = info;
	super.requirement = requirement;
    }

    public KeyOption(Key command, Info info) {
	this.flag = new Key("");
	this.key = command;
	super.info = info;
    }

    @Override
    public void accept(OptionVisitor visitor) {
	visitor.visit(this);
    }

    public Key getFlag() {
	return this.flag;
    }

    @Override
    public Key getKey() {
	return this.key;
    }

    @Override
    protected String buildString() {
	return String.format("KeyOption(Key(\"%s%s\"), Info(\"%s\"), Requirement(%s))", this.getFlag().toString(),
		this.getKey().toString(), this.getInfo().toString(), this.getRequirement().toString());
    }
}
