package com.volcano.campsite.repositories;

import com.volcano.campsite.CampsiteApplication;
import com.volcano.campsite.entities.Reservation;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = CampsiteApplication.class)
@Rollback
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@FlywayTest
class ReservationRepositoryTests {

	@Autowired
	DatabaseClient databaseClient;

	@Autowired
	ReservationRepository reservationRepository;

	@FlywayTest
	@Test
	void testRunsTestMigration() {
		assertThat(
			databaseClient.execute("SELECT first_name FROM reservations WHERE email = :email")
				.bind("email", "jdoe@volcano.com")
				.map((row, rowMetadata) -> row.get("first_name"))
				.one()
				.block()
		).isEqualTo("John");
	}

	@Test
	void testFindByEmail() {
		Flux<Reservation> reservation = reservationRepository.findByEmail("jdoe@volcano.com");
		Reservation found = reservation.next().block();

		assertThat(found).isNotNull();
		assertFound(found);
	}

	@Test
	void testFindByDateRangeOverlappingArrival() {
		// Reservation done from "2019-12-05" to "2019-12-08"
		LocalDate from = LocalDate.of(2019, 12, 4);
		LocalDate to = LocalDate.of(2019, 12, 6);

		Reservation found = findReservationByDateRange(from, to);

		assertFound(found);
	}

	@Test
	void testFindByDateRangeOverlappingDeparture() {
		// Reservation done from "2019-12-05" to "2019-12-08"
		LocalDate from = LocalDate.of(2019, 12, 7);
		LocalDate to = LocalDate.of(2019, 12, 9);

		Reservation found = findReservationByDateRange(from, to);

		assertFound(found);
	}

	@Test
	void testFindByDateRangeOverlappingBothArrivalAndDeparture() {
		// Reservation done from "2019-12-05" to "2019-12-08"
		LocalDate from = LocalDate.of(2019, 12, 6);
		LocalDate to = LocalDate.of(2019, 12, 7);

		Reservation found = findReservationByDateRange(from, to);

		assertFound(found);
	}

	@Test
	void testFindByDateRangeNotOverlappingBeforeArrival() {
		// Reservation done from "2019-12-05" to "2019-12-08"
		LocalDate from = LocalDate.of(2019, 12, 4);
		LocalDate to = LocalDate.of(2019, 12, 5);

		Reservation found = findReservationByDateRange(from, to);

		assertThat(found).isNull();
	}

	@Test
	void testFindByDateRangeNotOverlappingAfterDeparture() {
		// Reservation done from "2019-12-05" to "2019-12-08"
		LocalDate from = LocalDate.of(2019, 12, 8);
		LocalDate to = LocalDate.of(2019, 12, 9);

		Reservation found = findReservationByDateRange(from, to);

		assertThat(found).isNull();
	}

	private void assertFound(Reservation found) {
		assertThat(found.getUserEmail()).isEqualTo("jdoe@volcano.com");
		assertThat(found.getUserFirstName()).isEqualTo("John");
		assertThat(found.getUserLastName()).isEqualTo("Doe");
		assertThat(found.getArrivalDate()).isEqualTo(LocalDate.of(2019, 12, 5));
		assertThat(found.getDepartureDate()).isEqualTo(LocalDate.of(2019, 12, 8));
	}

	private Reservation findReservationByDateRange(LocalDate from, LocalDate to) {
		Flux<Reservation> reservation = reservationRepository.findByDateRange(from, to);
		return reservation.next().block();
	}
}
