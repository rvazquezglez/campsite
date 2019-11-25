package com.volcano.campsite.services;

import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationServiceTests {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void book() {
		// GIVEN a reservation request for more than one month in advance
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now().plus(1, DAYS);
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(arrivalDate.plus(2, DAYS));

		// WHEN trying to reserve
		new ReservationService().book(reservation);

		// THEN reservation is created


	}

	@Test
	void bookMoreThanThreeDays() {
		// GIVEN a reservation request for more than 3 days
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now().plus(2, DAYS);
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(arrivalDate.plus(5, DAYS));

		// WHEN trying to reserve
		Executable executable = () -> new ReservationService().book(reservation);

		// THEN
		String exceptionMessage = Assertions.assertThrows(
			ReservationOutOfRangeException.class,
			executable
		).getMessage();

		assertEquals("The campsite can be reserved for max 3 days. 5 days selected.",
			exceptionMessage);
	}

	@Test
	void bookLessThanOneDayOfArrival() {
		// GIVEN a reservation request less than 1 day ahead of arrival
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now();
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(arrivalDate.plus(2, DAYS));

		// WHEN trying to reserve
		Executable executable = () -> new ReservationService().book(reservation);

		// THEN
		String exceptionMessage = Assertions.assertThrows(
			ReservationOutOfRangeException.class,
			executable
		).getMessage();

		assertEquals("The campsite can be reserved minimum 1 day(s) ahead of arrival. Less than one day ahead selected.",
			exceptionMessage);
	}

	@Test
	void bookMoreThanOneMonthInAdvance() {
		// GIVEN a reservation request for more than one month in advance
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now().plus(1, MONTHS);
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(arrivalDate.plus(2, DAYS));

		// WHEN trying to reserve
		Executable executable = () -> new ReservationService().book(reservation);

		// THEN
		String exceptionMessage = Assertions.assertThrows(
			ReservationOutOfRangeException.class,
			executable
		).getMessage();

		assertEquals("The campsite can be reserved up to 1 month in advance. More than one month in advance selected.",
			exceptionMessage);
	}
}