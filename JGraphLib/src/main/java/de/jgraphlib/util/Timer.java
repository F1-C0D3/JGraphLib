package de.jgraphlib.util;

public class Timer {

	private Time startTime;
	private Time endTime;
	private Time duration;

	public Timer() {}

	public void start() {
		this.startTime = new Time(System.currentTimeMillis());
	}
	
	public void reset() {
		start();
	}
	
	public void stop() {
		this.endTime = new Time(System.currentTimeMillis());
		this.duration = new Time(this.endTime.getMillis() - startTime.getMillis());
	}

	public Time getTime() {
		return new Time(System.currentTimeMillis() - startTime.getMillis());
	}

	public Time getTime(long milliSeconds) {
		return new Time(milliSeconds - this.startTime.getMillis());
	}

	public Time getDuration() {
		return this.duration;
	}

	public class Time implements Comparable<Time> {

		private long milliSeconds = 0;

		public Time(long milliSeconds) {
			this.milliSeconds = milliSeconds;
		}

		public long getMillis() {
			return this.milliSeconds;
		}

		@Override
		public String toString() {
			long milliSeconds = this.milliSeconds % 1000;
			long seconds = (this.milliSeconds / 1000) % 60;
			long minutes = (this.milliSeconds / (1000 * 60)) % 60;
			long hours = (this.milliSeconds / (1000 * 60 * 60)) % 24;
			return String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, milliSeconds);
		}

		@Override
		public int compareTo(Time time) {
			return Double.compare(this.getMillis(), time.getMillis());
		}
	}
}
