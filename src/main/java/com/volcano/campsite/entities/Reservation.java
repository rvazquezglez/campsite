package com.volcano.campsite.entities;

import java.time.LocalDate;

public class Reservation {
	private String uniqueBookingIdentifier;

	private LocalDate arrivalDate;

	private LocalDate departureDate;

	private String userEmail;

	private String userFirstName;

	private String userLastName;

	public String getUniqueBookingIdentifier() {
		return uniqueBookingIdentifier;
	}

	public void setUniqueBookingIdentifier(String uniqueBookingIdentifier) {
		this.uniqueBookingIdentifier = uniqueBookingIdentifier;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
}
