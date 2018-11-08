package com.iam404.restkeyvaluevault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@EnableAutoConfiguration
@Component
@ComponentScan
@SpringBootApplication
@Configuration
public class kvStore {

	public static void main(String[] args) {

		SpringApplication.run(kvStore.class, args);
		System.out.format("This is starting of the application");

	}
}
