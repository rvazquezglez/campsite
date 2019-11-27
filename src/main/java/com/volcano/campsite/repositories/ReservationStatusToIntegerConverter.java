package com.volcano.campsite.repositories;

import com.volcano.campsite.entities.Reservation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ReservationStatusToIntegerConverter implements Converter<Reservation.Status, Integer> {
	@Override
	public Integer convert(Reservation.Status source) {
		return source.getCode();
	}
}
