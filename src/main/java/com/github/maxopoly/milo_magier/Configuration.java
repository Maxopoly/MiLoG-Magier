package com.github.maxopoly.milo_magier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Configuration for the generation of a single document instance
 * 
 * All time units are in milliseconds
 * 
 * @author max
 *
 */
public class Configuration {

	private long timeToWork;

	private long smallestFractionAllowed;

	private long minDurationPerSession;
	private long maxDurationPerSession;

	private long earliestTimeToBeginDuringDay;

	private long latestTimeToFinishDuringDay;

	private boolean workOnWeekends;

	private long averageSessionDuration;

	private int year;
	private int month;

	private List<String> tasks;

	public Configuration(long timeToWork, long smallestFractionAllowed, long minDurationPerSession,
			long maxDurationPerSession, long earliestTimeToBeginDuringDay, long latestTimeToFinishDuringDay,
			boolean workOnWeekends, long averageSessionDuration, int year, int month, Collection<String> tasks) {
		this.timeToWork = timeToWork;
		this.smallestFractionAllowed = smallestFractionAllowed;
		this.minDurationPerSession = minDurationPerSession;
		this.maxDurationPerSession = maxDurationPerSession;
		this.earliestTimeToBeginDuringDay = earliestTimeToBeginDuringDay;
		this.latestTimeToFinishDuringDay = latestTimeToFinishDuringDay;
		this.workOnWeekends = workOnWeekends;
		this.averageSessionDuration = averageSessionDuration;
		this.year = year;
		this.month = month;
		this.tasks = new ArrayList<>(tasks);
	}

	/**
	 * @return How many milliseconds should we be working
	 */
	public long getTimeToWork() {
		return timeToWork;
	}

	/**
	 * Describes the smallest interval based on which time will be subdivided. For
	 * example if this is set to 15 minutes, valid beginning/end for activities are
	 * X:0, X:15, X:30, X:45 minutes
	 * 
	 * @return Time granularity in milliseconds
	 */
	public long getSmallestFractionAllowed() {
		return smallestFractionAllowed;
	}

	/**
	 * @return How long may a single session last at maximum in milliseconds
	 */
	public long getMinimumSessionDuration() {
		return minDurationPerSession;
	}

	/**
	 * @return How long may a single session last at maximum in milliseconds
	 */
	public long getMaximumSessionDuration() {
		return maxDurationPerSession;
	}

	/**
	 * When are we allowed to start working. If this value is set to 8 hours, we
	 * won't start working before 8 am
	 * 
	 * @return Earliest possible work begin in milli seconds
	 */
	public long getEarliestTimeToBeginDuringDay() {
		return earliestTimeToBeginDuringDay;
	}

	/**
	 * What's the latest time during which we are allowed to finish working. If this
	 * value is set to 20 hours, we won't work past 8 PM
	 * 
	 * @return Latest possible end of an activity in milli seconds
	 */
	public long getLatestTimeToFinishDuringDay() {
		return latestTimeToFinishDuringDay;
	}

	/**
	 * @return How long a single session will taken on average in milliseconds
	 */
	public long getAverageSessionDuration() {
		return averageSessionDuration;
	}

	/**
	 * @return Are we allowed to work on weekends
	 */
	public boolean workOnWeekends() {
		return workOnWeekends;
	}

	/**
	 * @return Year during which we worked
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return Month during which we worked
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return Set of task descriptions to use
	 */
	public Collection<String> getTasks() {
		return tasks;
	}

}
