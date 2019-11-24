package com.upgrade.campsite.controllers.dtos;

public class ReservationSuccessResponse {
	private String uniqueBookingIdentifier;

	public String getUniqueBookingIdentifier() {
		return uniqueBookingIdentifier;
	}

	public void setUniqueBookingIdentifier(String uniqueBookingIdentifier) {
		this.uniqueBookingIdentifier = uniqueBookingIdentifier;
	}
}
