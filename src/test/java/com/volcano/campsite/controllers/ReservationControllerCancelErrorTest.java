package com.volcano.campsite.controllers;

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

import static com.volcano.campsite.controllers.ReservationTestUtil.getErrorResponseFieldsDoc;
import static com.volcano.campsite.controllers.ReservationTestUtil.getWebTestClient;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class ReservationControllerCancelErrorTest {

	@MockBean
	private BookingService bookingService;

	private WebTestClient webTestClient;

	@BeforeEach
	void setUp(
		ApplicationContext applicationContext,
		RestDocumentationContextProvider restDocumentation
	) {
		this.webTestClient = getWebTestClient(applicationContext, restDocumentation);

		when(bookingService.cancel(any())).thenReturn(Mono.error(new RuntimeException("Error cancelling")));
	}

	@Test
	void errorReservationCancellationTest() {
		// GIVEN
		Integer bookingId = 123;

		// WHEN
		WebTestClient.ResponseSpec exchange = this.webTestClient.delete()
			.uri("/reservations/{bookingId}", bookingId)
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().is5xxServerError()
			.expectBody()
			.consumeWith(
				document(
					"errorReservationCancellation",
					getErrorResponseFieldsDoc()
				)
			);
	}

}
