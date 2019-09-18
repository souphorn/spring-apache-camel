package com.codisystem.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamelApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CamelApplication.class, args);
	}

}
