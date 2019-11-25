package com.volcano.campsite.repositories;

import com.volcano.campsite.entities.Reservation;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface ReservationRepository extends ReactiveCrudRepository<Reservation, Integer> {
	@Query("SELECT * FROM reservations WHERE email = :email")
	Flux<Reservation> findByEmail(String email);

	@Query(
		"SELECT * FROM reservations "
			+ "WHERE ("
			+ "arrival_date >= :arrivalDate "
			+ "AND "
			+ "arrival_date < :departureDate"
			+ ") "
			+ "OR ( "
			+ "departure_date > :arrivalDate "
			+ "AND "
			+ "departure_date <= :departureDate"
			+ ")"
			+ "OR ( "
			+ "arrival_date <= :arrivalDate "
			+ "AND "
			+ "departure_date >= :departureDate"
			+ ")")
	Flux<Reservation> findByDateRange(LocalDate arrivalDate, LocalDate departureDate);
}
