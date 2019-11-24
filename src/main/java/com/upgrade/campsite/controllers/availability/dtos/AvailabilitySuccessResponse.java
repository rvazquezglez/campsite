package com.upgrade.campsite.controllers.availability.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upgrade.campsite.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

public class AvailabilitySuccessResponse {
	private String message;
	private List<DateRange> notAvailableDates;

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public List<DateRange> getNotAvailableDates() {
		return notAvailableDates;
	}

	public void setNotAvailableDates(List<DateRange> notAvailableDates) {
		this.notAvailableDates = notAvailableDates;
	}

	public static class DateRange {

		public DateRange() {
		}

		public DateRange(LocalDate initialDate, LocalDate endDate) {
			this.initialDate = initialDate;
			this.endDate = endDate;
		}

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_PATTERN)
		private LocalDate initialDate;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_PATTERN)
		private LocalDate endDate;

		public LocalDate getInitialDate() {
			return initialDate;
		}

		public void setInitialDate(LocalDate initialDate) {
			this.initialDate = initialDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}

	}
}
