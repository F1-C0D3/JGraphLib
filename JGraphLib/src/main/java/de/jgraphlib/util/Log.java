package de.jgraphlib.util;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log extends Logger {

    final long start = System.currentTimeMillis();

    public Log() {

	super("MyLog.Log", null);

	final ConsoleHandler handler = new ConsoleHandler() {
	    @Override
	    protected void setOutputStream(final OutputStream out) throws SecurityException {
		super.setOutputStream(System.out);
	    }
	};

	final Formatter formatter = new Formatter() {
	    @Override
	    public String format(LogRecord record) {
		long timespan = record.getMillis() - start;
		long milliSeconds = timespan % 1000;
		long seconds = (timespan / 1000) % 60;
		long minutes = (timespan / (1000 * 60)) % 60;
		long hours = (timespan / (1000 * 60 * 60)) % 24;
		return String.format("%02d:%02d:%02d.%d [%s]: %s%n", hours, minutes, seconds, milliSeconds,
			record.getLevel(), record.getMessage());
	    }
	};

	handler.setFormatter(formatter);
	handler.setLevel(Level.ALL);

	this.addHandler(handler);
	this.setUseParentHandlers(false);
	this.setLevel(Level.ALL);
    }

    public void infoHeader(HeaderLevel headerLevel, String str) {

	switch (headerLevel) {
	case h1:
	    this.info(String.format("### %s %s", str, "#".repeat(150 - str.length())));
	    break;
	case h2:
	    this.info(String.format("--- %s %s", str, "-".repeat(150 - str.length())));
	    break;
	case h3:
	    this.info(String.format("___ %s %s", str, "_".repeat(75 - str.length())));
	    break;
	}
    }

    public enum HeaderLevel {
	h1, h2, h3
    }
}
