package com.volcano.campsite.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Table("reservations")
public class Reservation {

	@Id
	@Column("ubi")
	private Integer uniqueBookingIdentifier;

	@Column("arrival_date")
	private LocalDate arrivalDate;

	@Column("departure_date")
	private LocalDate departureDate;

	@Column("email")
	private String userEmail;

	@Column("first_name")
	private String userFirstName;

	@Column("last_name")
	private String userLastName;

	@Column("status")
	private Status status;

	public Integer getUniqueBookingIdentifier() {
		return uniqueBookingIdentifier;
	}

	public void setUniqueBookingIdentifier(Integer uniqueBookingIdentifier) {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		UNKNOWN(0),
		ACTIVE(1),
		CANCELLED(2);

		private static Map<Integer, Status> statusMap;

		static {
			statusMap = Arrays.stream(Status.values())
				.collect(Collectors.toMap(Status::getCode, Function.identity()));
		}

		Status(int statusCode) {
			this.code = statusCode;
		}

		private final int code;

		public int getCode() {
			return code;
		}

		public static Status fromCode(int statusCode) {
			return statusMap.getOrDefault(statusCode, UNKNOWN);
		}
	}
}
