package com.volcano.campsite.controllers;

import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.volcano.campsite.controllers.ReservationTestUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class ReservationControllerUpdateSuccessTest {
	@MockBean
	private BookingService bookingService;

	private WebTestClient webTestClient;

	@BeforeEach
	void setUp(
		ApplicationContext applicationContext,
		RestDocumentationContextProvider restDocumentation
	) {
		this.webTestClient = getWebTestClient(applicationContext, restDocumentation);

		Reservation reservation = new Reservation();
		reservation.setUniqueBookingIdentifier(456);
		reservation.setStatus(Reservation.Status.ACTIVE);
		when(bookingService.update(any(), any())).thenReturn(Mono.just(reservation));
	}

	@Test
	void successfulReservationUpdateTest() {
		// GIVEN
		ReservationRequest newReservationRequest = reservationRequestFor("John");
		Integer bookingId = 456;

		// WHEN
		WebTestClient.ResponseSpec exchange = this.webTestClient.put()
			.uri("/reservations/{bookingId}", bookingId)
			.body(Mono.just(newReservationRequest), ReservationRequest.class)
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().isOk()
			.expectBody()
			.consumeWith(
				document(
					"successfulReservationUpdate",
					getBookingIdPathParameterDoc(),
					getReservationRequestFieldsDoc(),
					getSuccessResponseDoc()
				)
			);
	}
}
