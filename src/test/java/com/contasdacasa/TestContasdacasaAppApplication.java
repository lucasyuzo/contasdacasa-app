package com.contasdacasa;

import org.springframework.boot.SpringApplication;

public class TestContasdacasaAppApplication {

    static void main(String[] args) {
        SpringApplication.from(ContasdacasaAppApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
