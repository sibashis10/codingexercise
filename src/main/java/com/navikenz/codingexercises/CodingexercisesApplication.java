package com.navikenz.codingexercises;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@SpringBootApplication
@OpenAPIDefinition(tags = @Tag(name = "contacts", description = "Contact management sample application"), info = @Info(title = "Navikenz Coding Exercise (Fullstack)", version = "${application-version}", description = "${application-description}", contact = @Contact(email="careers@navikenz.com")))
public class CodingexercisesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingexercisesApplication.class, args);
	}	
}
