package com.example.rqchallenge.employees.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Retryer;

@Configuration
public class EmployeesClientConfguration {
	@Value("${apiconfig.retries}")
	private int maxRetries;
	@Value("${apiconfig.maxtimeout}")
	private int maxTimeout;
	@Value("${apiconfig.timeout}")
	private int timeout;

	@Value("${apiconfig.baseurl}")

	@Bean
	public Retryer retryer() {
		return new Retryer.Default(TimeUnit.SECONDS.toMillis(timeout), TimeUnit.SECONDS.toMillis(maxTimeout),
				maxRetries);
	}
}
