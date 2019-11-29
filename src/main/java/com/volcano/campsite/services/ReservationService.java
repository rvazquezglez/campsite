package com.volcano.campsite.services;

import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.repositories.ReservationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static com.volcano.campsite.config.CacheConfiguration.RESERVATIONS_CACHE;
import static com.volcano.campsite.entities.Reservation.Status.ACTIVE;
import static com.volcano.campsite.entities.Reservation.Status.CANCELLED;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;

	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	// Not cached, for cached version use AvailabilityService
	public Flux<Reservation> findByDateRange(LocalDate arrivalDate, LocalDate departureDate) {
		return reservationRepository.findActiveByDateRange(ACTIVE.getCode(), arrivalDate, departureDate);
	}

	@CacheEvict(
		value = RESERVATIONS_CACHE,
		allEntries = true
	)
	public Mono<Reservation> save(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	@CacheEvict(
		value = RESERVATIONS_CACHE,
		allEntries = true
	)
	public Mono<Void> cancel(Integer bookingId) {
		return reservationRepository.updateStatusById(CANCELLED.getCode(), bookingId);
	}
}
