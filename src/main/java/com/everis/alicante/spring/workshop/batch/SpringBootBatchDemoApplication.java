package com.everis.alicante.spring.workshop.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class SpringBootBatchDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootBatchDemoApplication.class, args);
  }

}

