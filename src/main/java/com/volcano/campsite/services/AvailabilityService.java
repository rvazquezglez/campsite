package com.volcano.campsite.services;

import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.repositories.ReservationRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.volcano.campsite.config.CacheConfiguration.RESERVATIONS_CACHE;
import static com.volcano.campsite.entities.Reservation.Status.ACTIVE;

@Service
public class AvailabilityService {
	private final ReservationRepository reservationRepository;

	public AvailabilityService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Cacheable(
		value = RESERVATIONS_CACHE,
		key = "{#arrivalDate, #departureDate}"
	)
	public List<Reservation> findByDateRange(LocalDate arrivalDate, LocalDate departureDate) {
		return reservationRepository.findActiveByDateRange(ACTIVE.getCode(), arrivalDate, departureDate).collectList().block();
	}
}
