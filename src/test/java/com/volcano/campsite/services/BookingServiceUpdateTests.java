package com.volcano.campsite.services;

import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.util.DateUtil;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceUpdateTests {

	@Test
	void updateTest() {
		// GIVEN a "happy path" reservation request
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now().plus(1, DAYS);
		reservation.setArrivalDate(arrivalDate);
		LocalDate departureDate = arrivalDate.plus(2, DAYS);
		reservation.setDepartureDate(departureDate);

		ReservationService reservationService = mock(ReservationService.class);

		when(reservationService.findByDateRange(any(), any())).thenReturn(Flux.empty());
		Reservation saved = new Reservation();
		saved.setUniqueBookingIdentifier(12);
		when(reservationService.cancel(any())).thenReturn(Mono.empty());
		when(reservationService.save(any())).thenReturn(Mono.just(saved));

		// WHEN trying to reserve
		Mono<Reservation> reservationMono = new BookingService(reservationService).update(123, reservation);

		// THEN save reservation is called
		StepVerifier.create(reservationMono)
			.expectNextMatches(
				savedReservation -> savedReservation.getUniqueBookingIdentifier() > 0

			)
			.expectComplete()
			.verify();

		verify(reservationService, times(1)).save(any());
	}

	@Test
	void updateWithinExistingReservationTest() {
		// GIVEN a request for a range that has already been booked
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now().plus(1, DAYS);
		reservation.setArrivalDate(arrivalDate);
		LocalDate departureDate = arrivalDate.plus(2, DAYS);
		reservation.setDepartureDate(departureDate);

		Reservation existingReservation = new Reservation();
		existingReservation.setArrivalDate(arrivalDate);
		existingReservation.setDepartureDate(departureDate);

		ReservationService reservationService = mock(ReservationService.class);
		when(reservationService.findByDateRange(any(), any())).thenReturn(Flux.just(existingReservation));
		when(reservationService.cancel(any())).thenReturn(Mono.empty());

		// WHEN trying to reserve
		Mono<Reservation> reservationMono = new BookingService(reservationService).update(123, reservation);

		// THEN error expected
		StepVerifier.create(reservationMono)
			.expectErrorMatches(
				throwable ->
					("Date range from "
						+ DateUtil.format(arrivalDate)
						+ " to "
						+ DateUtil.format(departureDate)
						+ " already booked.")
						.equals(throwable.getMessage())
			)
			.verify();
	}

	@Test
	void updateMoreThanThreeDaysTest() {
		// GIVEN a reservation request for more than 3 days
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now().plus(2, DAYS);
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(arrivalDate.plus(5, DAYS));

		ReservationService reservationService = mock(ReservationService.class);
		when(reservationService.cancel(any())).thenReturn(Mono.empty());

		// WHEN trying to reserve
		Mono<Reservation> reservationMono = new BookingService(reservationService).update(123, reservation);

		// THEN
		StepVerifier.create(reservationMono)
			.expectErrorMatches(
				throwable ->
					"The campsite can be reserved for max 3 days. 5 days selected."
						.equals(throwable.getMessage())
			)
			.verify();
	}

	@Test
	void updateLessThanOneDayOfArrivalTest() {
		// GIVEN a reservation request less than 1 day ahead of arrival
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now();
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(arrivalDate.plus(2, DAYS));

		ReservationService reservationService = mock(ReservationService.class);
		when(reservationService.cancel(any())).thenReturn(Mono.empty());

		// WHEN trying to reserve
		Mono<Reservation> reservationMono = new BookingService(reservationService).update(123, reservation);

		// THEN
		StepVerifier.create(reservationMono)
			.expectErrorMatches(
				throwable ->
					"The campsite can be reserved minimum 1 day(s) ahead of arrival. Less than one day ahead selected."
						.equals(throwable.getMessage())
			)
			.verify();

	}

	@Test
	void updateMoreThanOneMonthInAdvanceTest() {
		// GIVEN a reservation request for more than one month in advance
		ReservationRequest reservation = new ReservationRequest();
		LocalDate arrivalDate = LocalDate.now().plus(1, MONTHS);
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(arrivalDate.plus(2, DAYS));

		ReservationService reservationService = mock(ReservationService.class);
		when(reservationService.cancel(any())).thenReturn(Mono.empty());

		// WHEN trying to reserve
		Mono<Reservation> reservationMono = new BookingService(reservationService).update(123, reservation);

		// THEN
		StepVerifier.create(reservationMono)
			.expectErrorMatches(
				throwable ->
					"The campsite can be reserved up to 1 month in advance. More than one month in advance selected."
						.equals(throwable.getMessage())
			)
			.verify();
	}
}
