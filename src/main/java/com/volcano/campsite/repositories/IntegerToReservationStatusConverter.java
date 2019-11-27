package com.volcano.campsite.repositories;


import com.volcano.campsite.entities.Reservation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class IntegerToReservationStatusConverter implements Converter<Integer, Reservation.Status> {
	@Override
	public Reservation.Status convert(Integer source) {
		return Reservation.Status.fromCode(source);
	}
}
