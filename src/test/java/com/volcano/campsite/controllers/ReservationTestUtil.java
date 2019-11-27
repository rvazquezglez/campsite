package com.volcano.campsite.controllers;

import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import com.volcano.campsite.util.DateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.volcano.campsite.util.DateUtil.DATE_TIME_FORMATTER;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

class ReservationTestUtil {
	static ReservationRequest reservationRequestFor(String userFirstName) {
		DateTimeFormatter inputFormatter = DATE_TIME_FORMATTER;
		ReservationRequest newReservationRequest = new ReservationRequest();

		newReservationRequest.setArrivalDate(LocalDate.parse("2019-11-20", inputFormatter));
		newReservationRequest.setDepartureDate(LocalDate.parse("2019-11-21", inputFormatter));
		newReservationRequest.setUserFirstName(userFirstName);
		newReservationRequest.setUserLastName("Doe");
		newReservationRequest.setUserEmail("jdoe@volcano.com");
		return newReservationRequest;
	}

	static RequestFieldsSnippet getReservationRequestFieldsDoc() {
		return requestFields(
			fieldWithPath("arrivalDate").description(
				"The date that user is expected to come to the volcano. It needs to be at least 1 day after"
					+ " the booking date and up to 1 month after it. Expected format is \""
					+ DateUtil.DATE_PATTERN
					+ "\"."
			),
			fieldWithPath("departureDate").description(
				"The date that user is expected to leave the volcano. It can max 3 days after arrival. "
					+ "Expected format is \""
					+ DateUtil.DATE_PATTERN + "\"."
			),
			fieldWithPath("userEmail").description("The user's email. It needs to be a valid email."),
			fieldWithPath("userFirstName").description("The user's first name."),
			fieldWithPath("userLastName").description("The user's last name.")
		);
	}

	static ResponseFieldsSnippet getSuccessResponseDoc() {
		return responseFields(
			fieldWithPath("message")
				.description("Message response for when the delete is successful"),
			fieldWithPath("uniqueBookingIdentifier")
				.description("The unique identifier for booking when the reservation operation is successful.")
		);
	}

	static PathParametersSnippet getBookingIdPathParameterDoc() {
		return pathParameters(
			parameterWithName("bookingId")
				.description("The unique booking identifier for the reservation to be modified.")
		);
	}

	static ResponseFieldsSnippet getErrorResponseFieldsDoc() {
		return responseFields(
			fieldWithPath("error").description("A short error name."),
			fieldWithPath("errorDescription").description("Error description.")
		);
	}

	static WebTestClient getWebTestClient(ApplicationContext applicationContext, RestDocumentationContextProvider restDocumentation) {
		return WebTestClient.bindToApplicationContext(applicationContext)
			.configureClient()
			.filter(documentationConfiguration(restDocumentation))
			.build();
	}
}
