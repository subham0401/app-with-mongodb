package com.shubham;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudWithMongoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudWithMongoDbApplication.class, args);

		System.out.println("************Server Started*****************");
	}

}