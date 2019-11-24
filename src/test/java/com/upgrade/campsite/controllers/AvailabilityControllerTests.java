package com.upgrade.campsite.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class AvailabilityControllerTests {

	private WebTestClient webTestClient;

	@BeforeEach
	void setUp(
		ApplicationContext applicationContext,
		RestDocumentationContextProvider restDocumentation
	) {
		this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
			.configureClient()
			.filter(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	void testAvailabilityByDateRange() {
		// GIVEN a date range
		String arrivalDate = "2019-11-25";
		String departureDate = "2019-12-30";

		// WHEN checking for availability
		WebTestClient.ResponseSpec exchange = this.webTestClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/availability")
				.queryParam("arrivalDate", arrivalDate)
				.queryParam("departureDate", departureDate)
				.build())
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().isOk()
			.expectBody()
			.consumeWith(
				document(
					"availabilityByDateRange",
					requestParameters(
						parameterWithName("arrivalDate").description("Initial date for date range").optional(),
						parameterWithName("departureDate").description("End date for date range").optional()
					)
				)
			);
	}

	@Test
	void testAvailabilityWithoutDateRange() {
		// GIVEN NO date range


		// WHEN checking for availability
		WebTestClient.ResponseSpec exchange = this.webTestClient.get()
			.uri("/availability")
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

		// THEN
		exchange.expectStatus().isOk()
			.expectBody();
	}
}
