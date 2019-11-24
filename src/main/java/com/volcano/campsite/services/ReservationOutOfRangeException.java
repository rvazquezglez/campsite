package com.volcano.campsite.services;

public class ReservationOutOfRangeException extends RuntimeException {
	public ReservationOutOfRangeException(String message) {
		super(message);
	}
}
