package com.upgrade.campsite.controllers;

import com.upgrade.campsite.controllers.dtos.CancelReservationSuccessResponse;
import com.upgrade.campsite.controllers.dtos.ReservationErrorResponse;
import com.upgrade.campsite.controllers.dtos.ReservationRequest;
import com.upgrade.campsite.controllers.dtos.ReservationSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@PostMapping
	public Mono<ResponseEntity<?>> book(@Valid @RequestBody ReservationRequest reservation) {
		if (reservation.getUserFirstName().equalsIgnoreCase("James")) {
			ReservationErrorResponse reservationErrorResponse = new ReservationErrorResponse();
			reservationErrorResponse.setError("No Availability");
			reservationErrorResponse.setErrorDescription("11/21/2019 is already booked");

			return Mono.just(ResponseEntity.status(500).body(reservationErrorResponse));
		}

		ReservationSuccessResponse response = new ReservationSuccessResponse();
		response.setUniqueBookingIdentifier("uniqueBookingIdentifier");
		return Mono.just(ResponseEntity.ok(response));
	}

	@DeleteMapping(path = "/{bookingId}")
	public Mono<ResponseEntity<?>> cancelBooking(@PathVariable String bookingId) {
		if (bookingId.equalsIgnoreCase("ABC123")) {
			ReservationErrorResponse reservationErrorResponse = new ReservationErrorResponse();
			reservationErrorResponse.setError("No Availability");
			reservationErrorResponse.setErrorDescription("11/21/2019 is already booked");

			return Mono.just(ResponseEntity.status(500).body(reservationErrorResponse));
		}

		CancelReservationSuccessResponse response = new CancelReservationSuccessResponse();
		response.setMessage("Reservation " + bookingId + " successfully cancelled.");
		return Mono.just(ResponseEntity.ok(response));
	}
}
