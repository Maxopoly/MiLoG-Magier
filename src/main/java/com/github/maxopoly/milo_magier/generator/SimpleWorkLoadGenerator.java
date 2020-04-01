package com.github.maxopoly.milo_magier.generator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.github.maxopoly.milo_magier.Configuration;
import com.github.maxopoly.milo_magier.WorkActivity;

public class SimpleWorkLoadGenerator implements IWorkLoadGenerator {

	// we implemented comparedTo of WorkActivity, which means we can put them in a
	// TreeSet and iteration of the TreeSet will return them ordered by our
	// comparator (time from earliest to latest)
	private TreeSet<WorkActivity> activities;
	private Random rng;
	
	public SimpleWorkLoadGenerator() {
		this.activities = new TreeSet<>();
		this.rng = new Random();
	}

	@Override
	public List<WorkActivity> generateWork(Configuration config) {
		ZonedDateTime dateTime = ZonedDateTime.of(config.getYear(), config.getMonth(), 1, 0, 0, 0, 0,
				ZoneId.systemDefault());
		YearMonth yearMonth = YearMonth.of(config.getYear(), config.getMonth());
		int daysInMonth = yearMonth.lengthOfMonth();
		// how many work slots do we want to have
		int desiredSlots = (int) (config.getTimeToWork() / config.getAverageSessionDuration());
		TreeSet<Integer> days = new TreeSet<>();
		//we keep multiplying by 9, because the result seems random and no month has a multiple of 9 as number of days
		int offset = rng.nextInt(9);
		while(days.size() < desiredSlots) {
			int monthDay = offset % daysInMonth;
			LocalDate date = yearMonth.atDay(monthDay+1);
			offset += 23;
			int day = date.get(ChronoField.DAY_OF_WEEK);
			if (!config.workOnWeekends() && day >= 6) {
				//weekend
				continue;
			}
			days.add(monthDay);
		}
		List<Integer> dayList = new ArrayList<>(days);
		int timeLeftToAssign = (int) config.getTimeToWork();
		int sessionDurationRange = (int) ((config.getMaximumSessionDuration() - config.getMinimumSessionDuration())/config.getSmallestFractionAllowed());
		for(int i = 0; i < desiredSlots; i++) {
			if (timeLeftToAssign <= 0) {
				break;
			}
			long dayOffset = dayList.get(i) * TimeUnit.DAYS.toMillis(1) + dateTime.getLong(ChronoField.INSTANT_SECONDS) * 1000L;
			int timeForSlot = rng.nextInt(sessionDurationRange);
			long totalTime = config.getMinimumSessionDuration() + config.getSmallestFractionAllowed() * timeForSlot;
			totalTime = Math.min(totalTime, timeLeftToAssign);
			if (i == desiredSlots - 1) {
				totalTime = timeLeftToAssign;
			}
			timeLeftToAssign -= totalTime;
			long latestStartingPoint = config.getLatestTimeToFinishDuringDay() - totalTime;
			if (config.getEarliestTimeToBeginDuringDay() > latestStartingPoint) {
				throw new IllegalStateException("Created session which could not fit a time slot: " + latestStartingPoint);
			}
			long startTimeSelectionRange = latestStartingPoint - config.getEarliestTimeToBeginDuringDay();
			int startTimeSelectionSlots = (int) (startTimeSelectionRange / config.getSmallestFractionAllowed());
			int startingTimeSlot = rng.nextInt(startTimeSelectionSlots);
			long relativeStartingTime = config.getEarliestTimeToBeginDuringDay() + startingTimeSlot * config.getSmallestFractionAllowed();
			long relativeEndingTime = relativeStartingTime + totalTime;
			//pick random description from available ones
			String description = config.getTasks().stream().skip(rng.nextInt(config.getTasks().size())).findFirst().get();
			WorkActivity activity = new WorkActivity(relativeStartingTime + dayOffset, relativeEndingTime + dayOffset, 0, description);
			activities.add(activity);
		}
		return new ArrayList<>(activities);
	}

}
