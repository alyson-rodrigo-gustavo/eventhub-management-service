package br.com.alysongustavo.eventhubmanagementservice;

import org.springframework.boot.SpringApplication;

public class TestEventhubManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(EventhubManagementServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
