package de.jgraphlib.util.treeparser;

import java.util.Scanner;
import java.util.function.Consumer;

class State {

    Option option;
    Input input;

    public State(Option option, Input input) {
	this.option = option;
	this.input = input;
    }

    public Option getOption() {
	return this.option;
    }
}

public class TreeParser {

    private RootOption options;
    private State state;
    Consumer<String> outputListener;

    static String delimiter = " ";

    public TreeParser() {
	options = new RootOption();
    }

    public void addOption(Option option) {
	this.options.add(option);
    }

    public Option getOptions() {
	return this.options;
    }

    public Option findKeyOption(Option options, Key key) {
	for (Option option : options.getOptions()) {
	    if (key.equals(option.getKey()))
		return option;
	    else
		findKeyOption(option, key);
	}
	return null;
    }

    private void setState(Option option, Input input) {
	this.state = new State(option, input);
    }

    private State getState() {
	State state = this.state;
	this.state = null;
	return state;
    }

    private Boolean hasState() {
	return state != null;
    }

    public void addOutputListener(Consumer<String> outputListener) {
	this.outputListener = outputListener;
    }

    private void sendMessage(String string) {
	if (this.outputListener != null)
	    outputListener.accept(string);
    }

    public void match(String string) {
	consume(string, getOptions(), new Input());
    }

    private void consume(String string, Option option, Input input) {

	string = string.trim();

	if (this.hasState())
	    option = this.getState().getOption();

	if (string.isEmpty()) {
	    if (option.requiresOption()) {
		sendMessage(option.getOptions().toString());
		this.setState(option, input);
		return;
	    } else
		option.getFunction().getConsumer().accept(input);
	}

	for (KeyOption keyOption : option.getKeyOptions())
	    if (string.startsWith(keyOption.getFlag().toString() + keyOption.getKey().toString()))
		consume(string
			.substring(keyOption.getFlag().toString().length() + keyOption.getKey().toString().length()),
			keyOption, input);

	for (ValueOption valueOption : option.getValueOptions()) {
	    Scanner scanner = new Scanner(string);
	    scanner.skip("[^0-9]*");
	    String subString;
	    try {
		switch (valueOption.getValue().getType()) {
		case INT:
		    Integer intValue = scanner.nextInt();
		    input.INT.add(intValue);
		    consume(string.substring(intValue.toString().length()), valueOption, input);
		    break;
		case DOUBLE:
		    Double doubleValue = scanner.nextDouble();
		    input.DOUBLE.add(doubleValue);
		    consume(string.substring(doubleValue.toString().length()), valueOption, input);
		    break;
		case STRING:
		    subString = string.split(delimiter, 1)[0];
		    input.STRING.add(subString);
		    consume(string.substring(subString.length()), valueOption, input);
		    break;
		}
	    } catch (Exception e) {
	    }
	    scanner.close();
	}
    }
}
