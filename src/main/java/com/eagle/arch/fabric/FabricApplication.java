package com.eagle.arch.fabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class FabricApplication {

	public static void main(String[] args) {
		SpringApplication.run(FabricApplication.class, args);
	}

}
