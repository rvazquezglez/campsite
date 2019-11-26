package com.volcano.campsite.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final DateTimeFormatter DATE_TIME_FORMATTER;

	static {
		DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	}

	public static String format(LocalDate date) {
		return DATE_TIME_FORMATTER.format(date);
	}
}
