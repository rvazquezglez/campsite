package com.volcano.campsite.services;

import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

@Service
public class BookingService {

	private final ReservationService reservationService;

	public BookingService(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@Transactional("transactionManager")
	public Mono<Reservation> book(ReservationRequest reservationRequest) {
		Mono<Reservation> errorOrEmpty = performValidations(reservationRequest);
		return errorOrEmpty.switchIfEmpty(
			Mono.defer(
				() -> reservationService.save(reservationRequest.toReservation())
			)
		);
	}

	@Transactional("transactionManager")
	public Mono<Void> cancel(Integer bookingId) {
		return reservationService.cancel(bookingId);
	}


	@Transactional("transactionManager")
	public Mono<Reservation> update(Integer bookingId, ReservationRequest reservationRequest) {
		return reservationService.cancel(bookingId)
			.then(performValidations(reservationRequest)
				.switchIfEmpty(
					Mono.defer(
						() -> reservationService.save(reservationRequest.toReservation())
					)
				));
	}

	private Mono<Reservation> performValidations(ReservationRequest reservationRequest) {
		if (LocalDate.now().compareTo(reservationRequest.getArrivalDate()) >= 0) {
			return Mono.error(
				new ReservationOutOfRangeException(
					"The campsite can be reserved minimum 1 day(s) ahead of arrival. "
						+ "Less than one day ahead selected."
				)
			);
		}

		if (LocalDate.now().plus(1, MONTHS).compareTo(reservationRequest.getArrivalDate()) <= 0) {
			return Mono.error(
				new ReservationOutOfRangeException(
					"The campsite can be reserved up to 1 month in advance. More "
						+ "than one month in advance selected."
				)
			);
		}

		long selectedDays = DAYS.between(reservationRequest.getArrivalDate(), reservationRequest.getDepartureDate());
		if (selectedDays > 3) {
			return Mono.error(
				new ReservationOutOfRangeException(
					"The campsite can be reserved for max 3 days. "
						+ selectedDays
						+ " days selected."
				)
			);
		}

		return reservationService
			.findByDateRange(reservationRequest.getArrivalDate(), reservationRequest.getDepartureDate())
			.singleOrEmpty()
			.<Reservation>flatMap(reservation ->
				Mono.error(new ReservationOutOfRangeException(
					"Date range from "
						+ DateUtil.format(reservation.getArrivalDate())
						+ " to "
						+ DateUtil.format(reservation.getDepartureDate())
						+ " already booked."

				))
			);
	}
}
