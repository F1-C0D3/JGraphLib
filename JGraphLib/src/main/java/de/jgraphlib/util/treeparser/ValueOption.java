package de.jgraphlib.util.treeparser;

public class ValueOption extends Option {
    private Value value;

    public ValueOption(Value value, Info info, Function function, Requirement requirement) {
	this.value = value;
	super.info = info;
	super.function = function;
	super.requirement = requirement;
    }

    public ValueOption(Value value, Info info, Function function, Requirement requirement, Option option) {
	this.value = value;
	super.info = info;
	super.function = function;
	super.requirement = requirement;
	super.add(option);
    }

    public ValueOption(Value value, Info info, Function function) {
	this.value = value;
	super.info = info;
	super.function = function;
    }

    public ValueOption(Value value, Info info, Function function, Option option) {
	this.value = value;
	super.info = info;
	super.function = function;
	super.add(option);
    }

    public ValueOption(Value value, Info info, Requirement requirement) {
	this.value = value;
	super.info = info;
	super.requirement = requirement;
    }

    public ValueOption(Value value, Info info, Requirement requirement, Option option) {
	this.value = value;
	super.info = info;
	super.requirement = requirement;
	super.add(option);
    }

    public ValueOption(Value value, Info info) {
	this.value = value;
	super.info = info;
    }

    public ValueOption(Value value, Info info, Option option) {
	this.value = value;
	super.info = info;
	super.add(option);
    }

    @Override
    public void accept(OptionVisitor visitor) {
	visitor.visit(this);
    }

    @Override
    public Key getKey() {
	return null;
    }

    public Value getValue() {
	return this.value;
    }

    @Override
    protected String buildString() {
	return String.format("ValueOpton(ValueType(\"%s\"), Info(\"%s\"), Requirement(%s)", value.getType().toString(),
		this.getInfo().toString(), this.getRequirement().toString());
    }
}
