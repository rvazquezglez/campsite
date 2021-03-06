package com.volcano.campsite.controllers.reservation;

import com.volcano.campsite.controllers.reservation.dtos.ReservationErrorResponse;
import com.volcano.campsite.controllers.reservation.dtos.ReservationRequest;
import com.volcano.campsite.controllers.reservation.dtos.ReservationSuccessResponse;
import com.volcano.campsite.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("/reservations")
public class ReservationController {

	private Logger logger = LoggerFactory.getLogger(ReservationController.class);

	private final BookingService reservationService;

	public ReservationController(BookingService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping
	public Mono<ResponseEntity<?>> book(@Valid @RequestBody ReservationRequest reservationRequest) {
		logger.debug("Got booking request {}.", reservationRequest);
		return reservationService.book(reservationRequest)
			.<ResponseEntity<?>>flatMap(reservation -> {
				ReservationSuccessResponse response = new ReservationSuccessResponse();
				Integer bookingId = reservation.getUniqueBookingIdentifier();
				response.setUniqueBookingIdentifier(bookingId);
				response.setMessage("Reservation " + bookingId + " successfully created.");

				logger.info("Booking completed successfully.");
				return Mono.just(ResponseEntity.ok(response));
			})
			.onErrorResume(throwable -> {
					logger.warn("Booking could not be completed. " + throwable.getMessage());

					return Mono.just(ResponseEntity.status(500).body(
						getReservationErrorResponse(
							"Not booked",
							"Could not create booking. " + throwable.getMessage()
						))
					);
				}
			);
	}

	@DeleteMapping(path = "/{bookingId}")
	public Mono<ResponseEntity<?>> cancelBooking(@PathVariable Integer bookingId) {
		return reservationService.cancel(bookingId)
			.<ResponseEntity<?>>then(Mono.defer(() -> {
				ReservationSuccessResponse response = new ReservationSuccessResponse();
				response.setUniqueBookingIdentifier(bookingId);
				response.setMessage("Reservation " + bookingId + " successfully cancelled.");
				return Mono.just(ResponseEntity.ok(response));
			}))
			.onErrorResume(throwable ->
				Mono.just(ResponseEntity.status(500).body(
					getReservationErrorResponse(
						"Not cancelled",
						"Reservation " + bookingId + " could not be cancelled. "
							+ throwable.getMessage()
					))
				)
			);
	}

	@PutMapping(path = "/{bookingId}")
	public Mono<ResponseEntity<?>> updateBooking(
		@PathVariable Integer bookingId,
		@Valid @RequestBody ReservationRequest reservationRequest
	) {
		return reservationService.update(bookingId, reservationRequest)
			.<ResponseEntity<?>>flatMap(reservation -> {
				ReservationSuccessResponse response = new ReservationSuccessResponse();
				response.setUniqueBookingIdentifier(reservation.getUniqueBookingIdentifier());
				response.setMessage(
					"Reservation successfully updated. New booking identifier is: "
						+ reservation.getUniqueBookingIdentifier()
				);
				return Mono.just(ResponseEntity.ok(response));
			})
			.onErrorResume(throwable ->
				Mono.just(ResponseEntity.status(500).body(
					getReservationErrorResponse(
						"Not updated",
						throwable.getMessage()
					))
				)
			);
	}

	private ReservationErrorResponse getReservationErrorResponse(String error, String errorDescription) {
		ReservationErrorResponse reservationErrorResponse = new ReservationErrorResponse();
		reservationErrorResponse.setError(error);
		reservationErrorResponse.setErrorDescription(errorDescription);
		return reservationErrorResponse;
	}
}
