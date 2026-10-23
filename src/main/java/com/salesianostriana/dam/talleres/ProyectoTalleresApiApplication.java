package com.salesianostriana.dam.talleres;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info =
	@Info(description = "Una API REST para gestionar los talleres culturales, sus organizadores y los participantes inscritos",
			version = "1.0",
			contact = @Contact(email = "perez.moign25@triana.salesianos.edu", name = "Ignacio Pérez"),
			license = @License(name = "CC BY"),
			title = "API de talleres culturales"
	)
)
@SpringBootApplication
public class ProyectoTalleresApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoTalleresApiApplication.class, args);
	}

}
