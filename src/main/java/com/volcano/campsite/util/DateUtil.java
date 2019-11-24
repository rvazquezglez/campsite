package com.volcano.campsite.util;

import java.time.format.DateTimeFormatter;

public class DateUtil {
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final DateTimeFormatter DATE_TIME_FORMATTER;

	static {
		DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	}
}
