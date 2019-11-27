package com.volcano.campsite.services;

import com.volcano.campsite.CampsiteApplication;
import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.repositories.ReservationRepository;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDate;
import java.util.List;

import static com.volcano.campsite.entities.Reservation.Status.ACTIVE;


@SpringBootTest(classes = CampsiteApplication.class)
@Rollback
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@FlywayTest
class AvailablityServiceIntegrationTests {
	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	ReservationService reservationService;

	@Autowired
	AvailabilityService availabilityService;

	@Autowired
	private CacheManager cacheManager;

	@BeforeEach
	void setup() {
		reservationRepository.deleteAll().block();

		Reservation john = new Reservation();
		john.setArrivalDate(LocalDate.of(2019, 11, 26));
		john.setDepartureDate(LocalDate.of(2019, 11, 28));
		john.setUserFirstName("John");
		john.setUserLastName("Doe");
		john.setUserEmail("jdoe@volcano.com");
		john.setStatus(ACTIVE);

		reservationRepository.save(john).block();

		cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
	}

	@Test
	void availIsCachedTest() {
		// GIVEN an existing record
		List<Reservation> availability = availabilityService
			.findByDateRange(
				LocalDate.of(2019, 11, 24),
				LocalDate.of(2019, 11, 30)
			);

		Assertions.assertEquals(1, availability.size());

		// WHEN inserting a new record without eviction
		Reservation jenny = new Reservation();
		jenny.setArrivalDate(LocalDate.of(2019, 11, 29));
		jenny.setDepartureDate(LocalDate.of(2019, 11, 30));
		jenny.setUserFirstName("Jenny");
		jenny.setUserLastName("Daniels");
		jenny.setUserEmail("jdaniels@volcano.com");

		reservationRepository.save(jenny).block();

		// THEN the results include the latest record
		availability = availabilityService
			.findByDateRange(
				LocalDate.of(2019, 11, 24),
				LocalDate.of(2019, 11, 30)
			);

		Assertions.assertEquals(1, availability.size());
	}

	@Test
	void availCacheEvictedTest() {
		// GIVEN an existing record
		List<Reservation> availability = availabilityService
			.findByDateRange(
				LocalDate.of(2019, 11, 24),
				LocalDate.of(2019, 11, 30)
			);

		Assertions.assertEquals(1, availability.size());

		// WHEN inserting a new record WITH eviction
		Reservation jenny = new Reservation();
		jenny.setArrivalDate(LocalDate.of(2019, 11, 29));
		jenny.setDepartureDate(LocalDate.of(2019, 11, 30));
		jenny.setUserFirstName("Jenny");
		jenny.setUserLastName("Daniels");
		jenny.setUserEmail("jdaniels@volcano.com");
		jenny.setStatus(ACTIVE);

		reservationService.save(jenny).block();

		// THEN the results from cache don't include the latest record
		availability = availabilityService
			.findByDateRange(
				LocalDate.of(2019, 11, 24),
				LocalDate.of(2019, 11, 30)
			);

		Assertions.assertEquals(2, availability.size());

	}
}
