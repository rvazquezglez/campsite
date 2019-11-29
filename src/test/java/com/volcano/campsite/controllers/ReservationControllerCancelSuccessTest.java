package com.volcano.campsite.controllers;

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
class ReservationControllerCancelSuccessTest {
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
		reservation.setUniqueBookingIdentifier(123);
		reservation.setStatus(Reservation.Status.CANCELLED);

		when(bookingService.cancel(any())).thenReturn(Mono.empty());
	}

	@Test
	void successfulReservationCancellationTest() {
		// GIVEN
		Integer bookingId = 123;

		// WHEN
		WebTestClient.ResponseSpec exchange = this.webTestClient.delete()
			.uri("/reservations/{bookingId}", bookingId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().isOk()
			.expectBody()
			.consumeWith(
				document(
					"successfulReservationCancellation",
					getBookingIdPathParameterDoc(),
					getSuccessResponseDoc()
				)
			);
	}

}
