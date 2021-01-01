package com.app.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ananth Shanmugam
 * Class to define entry point for spring boot to run
 */

@SpringBootApplication
public class BookStoreAppLauncher {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreAppLauncher.class, args);
	}

}
