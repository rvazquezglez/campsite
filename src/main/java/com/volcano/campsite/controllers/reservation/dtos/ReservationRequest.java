package com.volcano.campsite.controllers.reservation.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.volcano.campsite.entities.Reservation;
import com.volcano.campsite.util.DateUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public class ReservationRequest {
	@NotNull(message = "Arrival date should not be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_PATTERN)
	private LocalDate arrivalDate;

	@NotNull(message = "Departure date should not be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_PATTERN)
	private LocalDate departureDate;

	@Email(message = "Email should be valid")
	private String userEmail;

	@NotEmpty(message = "First name should not be empty")
	private String userFirstName;

	@NotEmpty(message = "Last name should not be empty")
	private String userLastName;

	public Reservation toReservation() {
		Reservation reservation = new Reservation();
		reservation.setArrivalDate(arrivalDate);
		reservation.setDepartureDate(departureDate);
		reservation.setUserEmail(userEmail);
		reservation.setUserFirstName(userFirstName);
		reservation.setUserLastName(userLastName);

		return reservation;
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

	@Override
	public String toString() {
		return "ReservationRequest{" +
			"arrivalDate=" + arrivalDate +
			", departureDate=" + departureDate +
			", userEmail='" + userEmail + '\'' +
			", userFirstName='" + userFirstName + '\'' +
			", userLastName='" + userLastName + '\'' +
			'}';
	}
}
