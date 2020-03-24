package com.github.maxopoly.milo_magier;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * Describes a single session of work
 * 
 * @author max
 *
 */
public class WorkActivity implements Comparable<WorkActivity>{
	
	private long beginning;
	private long end;
	private long includedBreak;
	private String description;
	
	public WorkActivity(long beginning, long end, long includedBreak, String description) {
		this.beginning = beginning;
		this.end = end;
		this.includedBreak = includedBreak;
		this.description = description;
	}

	/**
	 * @return Day of the month (1-31) on which we worked
	 */
	public int getDay() {
		Date date = new Date(beginning);
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.getDayOfMonth();
	}
	
	/**
	 * @return How many milliseconds after 0:00 did we start working
	 */
	public long getRelativeBeginningTime() {
		Date date = new Date(beginning);
		ZonedDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault());
		return localDate.getLong(ChronoField.MILLI_OF_DAY);
	}
	
	/**
	 * @return How many milliseconds after 0:00 were we done working
	 */
	public long getRelativeEndTime() {
		Date date = new Date(end);
		ZonedDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault());
		return localDate.getLong(ChronoField.MILLI_OF_DAY);
	}
	
	/**
	 * @return How many milliseconds of break were taken
	 */
	public long getBreakTime() {
		return includedBreak;
	}
	
	/**
	 * @return How many milli seconds was worked, already excluding the break time
	 */
	public long getWorkingTime() {
		return end - beginning - includedBreak;
	}
	
	/**
	 * @return Description of this activity
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public int compareTo(WorkActivity o) {
		return Long.compare(beginning, o.beginning);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof WorkActivity)) {
			return false;
		}
		WorkActivity activ = (WorkActivity) o;
		return activ.beginning == beginning && activ.end == end;
	}
	
	@Override
	public String toString() {
		return String.format("%s from %s to %s", description, formatDateTime(beginning), formatDateTime(end));
	}
	
	private static final String formatDateTime(long time) {
		return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()));
	}

}
