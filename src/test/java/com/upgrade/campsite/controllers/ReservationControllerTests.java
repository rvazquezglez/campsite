package com.upgrade.campsite.controllers;

import com.upgrade.campsite.controllers.dtos.ReservationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
public class ReservationControllerTests {

	private WebTestClient webTestClient;

	@BeforeEach
	public void setUp(
		ApplicationContext applicationContext,
		RestDocumentationContextProvider restDocumentation
	) {
		this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
			.configureClient()
			.filter(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	public void successfulReservationTest() {
		// GIVEN
		ReservationRequest newReservationRequest = reservationRequestFor("John");

		// WHEN
		WebTestClient.ResponseSpec exchange = this.webTestClient.post()
			.uri("/reservations")
			.body(Mono.just(newReservationRequest), ReservationRequest.class)
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().isOk()
			.expectBody()
			.consumeWith(
				document(
					"successfulReservation",
					getReservationRequestFieldsDoc(),
					getSuccessResponseDoc()
				)
			);
	}

	@Test
	public void errorReservationTest() {
		// GIVEN
		ReservationRequest newReservationRequest = reservationRequestFor("James");

		// WHEN
		WebTestClient.ResponseSpec exchange = this.webTestClient.post()
			.uri("/reservations")
			.body(Mono.just(newReservationRequest), ReservationRequest.class)
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().is5xxServerError()
			.expectBody()
			.consumeWith(
				document(
					"errorReservation",
					getErrorResponseFieldsDoc()
				)
			);
	}

	@Test
	public void successfulReservationCancellationTest() {
		// GIVEN
		String bookingId = "DEF456";

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

	@Test
	public void errorReservationCancellationTest() {
		// GIVEN
		String bookingId = "ABC123";

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

	@Test
	public void successfulReservationUpdateTest() {
		// GIVEN
		ReservationRequest newReservationRequest = reservationRequestFor("John");
		String bookingId = "DEF456";

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

	@Test
	public void errorReservationUpdateTest() {
		// GIVEN
		ReservationRequest newReservationRequest = reservationRequestFor("James");

		// WHEN
		WebTestClient.ResponseSpec exchange = this.webTestClient.post()
			.uri("/reservations")
			.body(Mono.just(newReservationRequest), ReservationRequest.class)
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().is5xxServerError()
			.expectBody()
			.consumeWith(
				document(
					"errorReservationUpdate",
					getErrorResponseFieldsDoc()
				)
			);
	}

	private PathParametersSnippet getBookingIdPathParameterDoc() {
		return pathParameters(
			parameterWithName("bookingId")
				.description("The unique booking identifier for the reservation to be cancelled.")
		);
	}

	private ReservationRequest reservationRequestFor(String userFirstName) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		ReservationRequest newReservationRequest = new ReservationRequest();

		newReservationRequest.setArrivalDate(LocalDate.parse("11/20/2019", inputFormatter));
		newReservationRequest.setDepartureDate(LocalDate.parse("11/21/2019", inputFormatter));
		newReservationRequest.setUserFirstName(userFirstName);
		newReservationRequest.setUserLastName("Doe");
		newReservationRequest.setUserEmail("jdoe@upgrade.com");
		return newReservationRequest;
	}

	private ResponseFieldsSnippet getErrorResponseFieldsDoc() {
		return responseFields(
			fieldWithPath("error").description("A short error name."),
			fieldWithPath("errorDescription").description("Error description.")
		);
	}

	private RequestFieldsSnippet getReservationRequestFieldsDoc() {
		return requestFields(
			fieldWithPath("arrivalDate").description(
				"The date that user is expected to come to the volcano. It needs to be at least 1 day after"
					+ " the booking date and up to 1 month after it. Expected format is \"dd/MM/yyyy\"."
			),
			fieldWithPath("departureDate").description(
				"The date that user is expected to leave the volcano. It can max 3 days after arrival. "
					+ "Expected format is \"dd/MM/yyyy\"."
			),
			fieldWithPath("userEmail").description("The user's email. It needs to be a valid email."),
			fieldWithPath("userFirstName").description("The user's first name."),
			fieldWithPath("userLastName").description("The user's last name.")
		);
	}

	private ResponseFieldsSnippet getSuccessResponseDoc() {
		return responseFields(
			fieldWithPath("message")
				.description("Message response for when the delete is successful"),
			fieldWithPath("uniqueBookingIdentifier")
				.description("The unique identifier for booking when the reservation operation is successful.")
		);
	}
}
