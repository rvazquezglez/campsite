package com.volcano.campsite.services;

import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

@Service
public class ReservationService {
	public void book(ReservationRequest reservation) {
		if (LocalDate.now().compareTo(reservation.getArrivalDate()) >= 0) {
			throw new ReservationOutOfRangeException("The campsite can be reserved minimum 1 day(s) ahead of arrival. "
				+ "Less than one day ahead selected.");
		}

		if (LocalDate.now().plus(1, MONTHS).compareTo(reservation.getArrivalDate()) <= 0) {
			throw new ReservationOutOfRangeException("The campsite can be reserved up to 1 month in advance. More "
				+ "than one month in advance selected.");
		}

		long selectedDays = DAYS.between(reservation.getArrivalDate(), reservation.getDepartureDate());
		if (selectedDays > 3) {
			throw new ReservationOutOfRangeException("The campsite can be reserved for max 3 days. "
				+ selectedDays
				+ " days selected.");
		}

		throw new RuntimeException("not implemented yet");
	}
}
