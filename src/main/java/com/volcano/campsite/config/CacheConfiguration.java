package com.volcano.campsite.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {
	public static final String RESERVATIONS_CACHE = "reservations";

	@Bean
	CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager(RESERVATIONS_CACHE);
		cacheManager.setCaffeine(Caffeine.newBuilder()
			.initialCapacity(50)
			.maximumSize(75)
			.expireAfterAccess(1, TimeUnit.HOURS));

		return cacheManager;
	}
}
