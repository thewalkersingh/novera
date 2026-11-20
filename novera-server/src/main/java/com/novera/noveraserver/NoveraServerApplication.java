package com.novera.noveraserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoveraServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(NoveraServerApplication.class, args);
		System.out.println("+----------------------------------+");
		System.out.println("|     (•_•)   App Started...!!     |");
		System.out.println("+----------------------------------+");
	}
	
}