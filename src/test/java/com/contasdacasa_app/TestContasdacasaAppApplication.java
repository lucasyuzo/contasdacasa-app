package com.contasdacasa_app;

import org.springframework.boot.SpringApplication;

public class TestContasdacasaAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContasdacasaAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
