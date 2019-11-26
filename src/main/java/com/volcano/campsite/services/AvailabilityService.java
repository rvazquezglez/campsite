package com.volcano.campsite.services;

import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.repositories.ReservationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static com.volcano.campsite.config.CacheConfiguration.RESERVATIONS_CACHE;

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
		return reservationRepository.findByDateRange(arrivalDate, departureDate).collectList().block();
	}
}
