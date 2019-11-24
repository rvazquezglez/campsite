package com.upgrade.campsite.controllers.reservation.dtos;

public class ReservationSuccessResponse {
	private String uniqueBookingIdentifier;
	private String message;

	public String getUniqueBookingIdentifier() {
		return uniqueBookingIdentifier;
	}

	public void setUniqueBookingIdentifier(String uniqueBookingIdentifier) {
		this.uniqueBookingIdentifier = uniqueBookingIdentifier;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
