package com.volcano.campsite.controllers;

import com.volcano.campsite.CampsiteApplication;
import com.volcano.campsite.controllers.reservation.ReservationController;
import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.repositories.ReservationRepository;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.time.temporal.ChronoUnit.DAYS;

@SpringBootTest(classes = CampsiteApplication.class)
@Rollback
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@FlywayTest
class ReservationControllerIntegrationTests {

	@Autowired
	private ReservationRepository repository;

	@Autowired
	private ReservationController reservationController;

	private WebTestClient webTestClient;


	@BeforeEach
	void setUp(
		ApplicationContext applicationContext
	) {
		this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
			.configureClient()
			.build();
	}


	@Test
	@Disabled
	void manyReservationSavedAtSameTime() throws InterruptedException {
		// GIVEN 3 concurrent threads doing the same reservation
		int numberOfConcurrentRequests = 3;
		CountDownLatch readyToStartLatch = new CountDownLatch(numberOfConcurrentRequests);
		CountDownLatch completedLatch = new CountDownLatch(numberOfConcurrentRequests);

		final String concurrentEmail = "concurrent@volcano.com";

		for (int i = 0; i < numberOfConcurrentRequests; i++) {
			Thread thread = new Thread(() -> {
				try {
					readyToStartLatch.await();

					ReservationRequest newReservationRequest = new ReservationRequest();
					LocalDate arrivalDate = LocalDate.now().plus(1, DAYS);
					newReservationRequest.setArrivalDate(arrivalDate);
					LocalDate departureDate = arrivalDate.plus(2, DAYS);
					newReservationRequest.setDepartureDate(departureDate);
					newReservationRequest.setUserEmail(concurrentEmail);

					reservationController.book(newReservationRequest).block();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					completedLatch.countDown();
				}
			});
			// WHEN running them
			thread.start();
			readyToStartLatch.countDown();
		}

		completedLatch.await();

		// THEN just one of them should be saved

		List<Reservation> reservations = repository.findByEmail(concurrentEmail).collectList().block();

		StepVerifier.create(repository.findByEmail(concurrentEmail))
			.expectNextMatches(reservation -> true)// just one, no more `expectNext`
			.expectComplete()
			.verify();
	}
}
