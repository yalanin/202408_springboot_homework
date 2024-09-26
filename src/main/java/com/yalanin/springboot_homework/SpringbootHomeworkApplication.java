package com.yalanin.springboot_homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootHomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHomeworkApplication.class, args);
	}

}
