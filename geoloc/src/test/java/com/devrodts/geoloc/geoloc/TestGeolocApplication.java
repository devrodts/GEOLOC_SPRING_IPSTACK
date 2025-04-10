package com.devrodts.geoloc.geoloc;

import org.springframework.boot.SpringApplication;

public class TestGeolocApplication {

	public static void main(String[] args) {
		SpringApplication.from(GeolocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
