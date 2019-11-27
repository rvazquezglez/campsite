package com.volcano.campsite.controllers.reservation.dtos;

public class ReservationSuccessResponse {
	private Integer uniqueBookingIdentifier;
	private String message;

	public Integer getUniqueBookingIdentifier() {
		return uniqueBookingIdentifier;
	}

	public void setUniqueBookingIdentifier(Integer uniqueBookingIdentifier) {
		this.uniqueBookingIdentifier = uniqueBookingIdentifier;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
