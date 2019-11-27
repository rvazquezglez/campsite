package com.volcano.campsite.config;

import com.volcano.campsite.repositories.IntegerToReservationStatusConverter;
import com.volcano.campsite.repositories.ReservationStatusToIntegerConverter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.Arrays;

import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;

@Configuration
public class R2dbcCustomConversionsConfig extends AbstractR2dbcConfiguration {
	@Override
	public ConnectionFactory connectionFactory() {
		return ConnectionFactories.get(ConnectionFactoryOptions.builder()
			.option(DRIVER, "h2")
			.build());
	}

	@Bean
	@Override
	public R2dbcCustomConversions r2dbcCustomConversions() {
		return new R2dbcCustomConversions(
			getStoreConversions(),
			Arrays.asList(
				new IntegerToReservationStatusConverter(),
				new ReservationStatusToIntegerConverter()
			));
	}
}
