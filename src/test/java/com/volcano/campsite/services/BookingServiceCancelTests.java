package com.volcano.campsite.services;

import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import com.volcano.campsite.entities.Reservation;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceCancelTests {

	@Test
	void cancelTest() {
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
		saved.setStatus(Reservation.Status.CANCELLED);
		when(reservationService.cancel(any())).thenReturn(Mono.empty());

		// WHEN trying to cancel
		Mono<Void> reservationMono = new BookingService(reservationService).cancel(123);

		// THEN save reservation is called
		StepVerifier.create(reservationMono)
			.expectComplete()
			.verify();

		verify(reservationService, times(1)).cancel(any());
	}
}
