package com.appdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Classe contenente il main dell'applicativo, il quale prevede l'avvio
 * dell'applicazione web.
 * 
 * @author Giulia Temperini, Paolo Cacciatore
 * @version 1.0
 */
@ComponentScan({ "com.app.controller", "com.app.service" })
@SpringBootApplication
public class MobileAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileAppApplication.class, args);
		
	}
}