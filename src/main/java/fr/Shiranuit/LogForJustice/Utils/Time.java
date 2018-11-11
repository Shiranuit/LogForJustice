package fr.Shiranuit.LogForJustice.Utils;

import java.util.Date;

public class Time {

	private long days = 0;
	private long hours = 0;
	private long minutes = 0;
	private long seconds = 0;
	private long milliseconds = 0;
	public Time(long ms){
		while (ms > 0) {
			if (ms - 86400000 >= 0) {
				ms -= 86400000;
				this.days++;
			} else if (ms - 3600000 >= 0) {
				ms -= 3600000;
				this.hours++;
			} else if (ms - 60000 >= 0) {
				ms-= 60000;
				this.minutes++;
			} else if (ms - 1000 >= 0) {
				ms -= 1000;
				this.seconds++;
			} else {
				this.milliseconds = ms;
				ms = 0;
			}
		}
	}
	
	public Time(Date date) {
		this(date.getTime());
	}
	
	public long getDays() {
		return this.days;
	}
	
	public long getHours() {
		return this.hours;
	}
	
	public long getMinutes() {
		return this.minutes;
	}
	
	public long getSeconds() {
		return this.seconds;
	}
	
	public long getMilliseconds() {
		return this.milliseconds;
	}
}
