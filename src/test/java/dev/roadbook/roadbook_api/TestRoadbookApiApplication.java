package dev.roadbook.roadbook_api;

import org.springframework.boot.SpringApplication;

public class TestRoadbookApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(RoadbookApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
