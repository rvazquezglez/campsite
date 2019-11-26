package com.volcano.campsite.controllers.availability;

import com.volcano.campsite.controllers.availability.dtos.AvailabilitySuccessResponse;
import com.volcano.campsite.services.AvailabilityService;
import com.volcano.campsite.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

import static com.volcano.campsite.util.DateUtil.DATE_TIME_FORMATTER;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

	private final AvailabilityService availabilityService;

	public AvailabilityController(AvailabilityService availabilityService) {
		this.availabilityService = availabilityService;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@GetMapping
	public Mono<ResponseEntity<?>> availability(
		@RequestParam("arrivalDate")
		@DateTimeFormat(pattern = DateUtil.DATE_PATTERN)
			Optional<LocalDate> arrivalDateOptional,
		@RequestParam("departureDate")
		@DateTimeFormat(pattern = DateUtil.DATE_PATTERN)
			Optional<LocalDate> departureDateOptional
	) {
		LocalDate arrivalDate = arrivalDateOptional.orElseGet(() -> LocalDate.now().plus(1, ChronoUnit.DAYS));
		LocalDate departureDate = arrivalDateOptional.orElseGet(() -> LocalDate.now().plus(1, ChronoUnit.MONTHS));

		AvailabilitySuccessResponse response = new AvailabilitySuccessResponse();
		response.setMessage("Availability from "
			+ DATE_TIME_FORMATTER.format(arrivalDate)
			+ " to "
			+ DATE_TIME_FORMATTER.format(departureDate)
			+ "."
		);
		response.setNotAvailableDates(Arrays.asList(
			new AvailabilitySuccessResponse.DateRange(LocalDate.of(2019, 11, 25), LocalDate.of(2019, 11, 26)),
			new AvailabilitySuccessResponse.DateRange(LocalDate.of(2019, 11, 27), LocalDate.of(2019, 11, 28))
		));

		return Mono.just(ResponseEntity.ok(response));
	}

}
