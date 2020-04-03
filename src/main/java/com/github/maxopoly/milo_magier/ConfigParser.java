package com.github.maxopoly.milo_magier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigParser {

	private Logger logger;

	public ConfigParser(Logger logger, String[] launchArgs) {
		this.logger = logger;
		Map<String, String> args = decodeStartupArgs(launchArgs);
		String configPath = args.get("config");
		if (configPath != null) {
			JSONObject json = loadJSON(configPath);
			insertConfigIntoMap(json, "", args);
		}
	}

	private Configuration parse(Map<String, String> args) {
		long timeToWork = parseTime(args.get("time_to_work"), "40h");
		long smallestFractionAllowed = parseTime(args.get("smallest_time_fraction"), "15m");
		long minDurationPerSession = parseTime(args.get("minimum_session_duration"), "1h");
		long maxDurationPerSession = parseTime(args.get("maximum_session_duration"), "4h");
		long earliestTimeToBeginDuringDay = parseTime(args.get("earliest_starting_time"), "8h");
		long latestTimeToFinishDuringDay = parseTime(args.get("latest_ending_time"), "17h");
		boolean workOnWeekends = Boolean.valueOf(args.getOrDefault("work_on_weekends", "false"));
		long averageSessionDuration = parseTime(args.get("average_session_duration"), "2h");
		int year = Integer.valueOf(args.getOrDefault("year", "2020"));
		int month = Integer.valueOf(args.getOrDefault("month", "3"));
		List<String> tasks = Arrays.asList(args.getOrDefault("tasks", "Software,Hardware").split(","));
		return new Configuration(timeToWork, smallestFractionAllowed, minDurationPerSession, maxDurationPerSession,
				earliestTimeToBeginDuringDay, latestTimeToFinishDuringDay, workOnWeekends, averageSessionDuration, year,
				month, tasks);
	}

	private static String getSuffix(String arg, Predicate<Character> selector) {
		StringBuilder number = new StringBuilder();
		for (int i = arg.length() - 1; i >= 0; i--) {
			if (selector.test(arg.charAt(i))) {
				number.insert(0, arg.substring(i, i + 1));
			} else {
				break;
			}
		}
		return number.toString();
	}

	public static long parseTime(String input, String defaultValue) {
		if (input == null) {
			input = defaultValue;
		}
		input = input.replace(" ", "").replace(",", "").toLowerCase();
		long result = 0;
		try {
			result += Long.parseLong(input);
			return result;
		} catch (NumberFormatException e) {
		}
		while (!input.equals("")) {
			String typeSuffix = getSuffix(input, a -> Character.isLetter(a));
			input = input.substring(0, input.length() - typeSuffix.length());
			String numberSuffix = getSuffix(input, a -> Character.isDigit(a));
			input = input.substring(0, input.length() - numberSuffix.length());
			long duration;
			if (numberSuffix.length() == 0) {
				duration = 1;
			} else {
				duration = Long.parseLong(numberSuffix);
			}
			switch (typeSuffix) {
			case "ms":
			case "milli":
			case "millis":
				result += duration;
				break;
			case "s": // seconds
			case "sec":
			case "second":
			case "seconds":
				result += TimeUnit.SECONDS.toMillis(duration);
				break;
			case "m": // minutes
			case "min":
			case "minute":
			case "minutes":
				result += TimeUnit.MINUTES.toMillis(duration);
				break;
			case "h": // hours
			case "hour":
			case "hours":
				result += TimeUnit.HOURS.toMillis(duration);
				break;
			case "d": // days
			case "day":
			case "days":
				result += TimeUnit.DAYS.toMillis(duration);
				break;
			case "w": // weeks
			case "week":
			case "weeks":
				result += TimeUnit.DAYS.toMillis(duration * 7);
				break;
			case "month": // weeks
			case "months":
				result += TimeUnit.DAYS.toMillis(duration * 30);
				break;
			case "never":
			case "inf":
			case "infinite":
			case "perm":
			case "perma":
			case "forever":
				// 1000 years counts as perma
				result += TimeUnit.DAYS.toMillis(365 * 1000);
			default:
				// just ignore it
			}
		}
		return result;
	}

	private void insertConfigIntoMap(JSONObject json, String prefix, Map<String, String> args) {
		for (String key : json.keySet()) {
			String value = json.optString(key);
			if (value != null) {
				args.putIfAbsent(key, prefix + value);
			} else {
				JSONObject subField = json.optJSONObject(key);
				if (subField == null) {
					logger.warn("Ignoring invalid config entry " + prefix + key);
				}
				insertConfigIntoMap(subField, prefix + "." + key, args);
			}
		}
	}

	private JSONObject loadJSON(String path) {
		final StringBuilder sb = new StringBuilder();
		try {
			Files.readAllLines(new File(path).toPath()).forEach(sb::append);
			return new JSONObject(sb.toString());
		} catch (IOException | JSONException e) {
			logger.error("Failed to load config file", e);
			return null;
		}
	}

	private static Map<String, String> decodeStartupArgs(String[] args) {
		Map<String, String> result = new HashMap<>();
		// see
		// https://stackoverflow.com/questions/7901978/regex-and-escaped-and-unescaped-delimiter?answertab=votes#tab-top
		Pattern regex = Pattern.compile("(?:\\\\.|[^=\\\\]++)*");
		for (String arg : args) {
			Matcher regexMatcher = regex.matcher(arg);
			if (!regexMatcher.find()) {
				throw new IllegalArgumentException(args + " could not be parsed as parameter, no key found");
			}
			String key = regexMatcher.group().toLowerCase();
			if (!regexMatcher.find()) {
				throw new IllegalArgumentException(args + " could not be parsed as parameter, no value found");
			}
			String value = regexMatcher.group();
			result.put(key, value);
		}
		return result;
	}
}
