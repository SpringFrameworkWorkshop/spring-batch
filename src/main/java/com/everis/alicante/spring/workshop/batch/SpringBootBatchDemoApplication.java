package com.everis.alicante.spring.workshop.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBootBatchDemoApplication {

  private static final Log logger = LogFactory.getLog(SpringBootBatchDemoApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(SpringBootBatchDemoApplication.class, args);
  }

  @Bean
  public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
    return jobBuilderFactory.get("job").start(
      stepBuilderFactory.get("step1").tasklet(((stepContribution, chunkContext) -> {
        logger.info("El job ha sido ejecutado");
        return RepeatStatus.FINISHED;
      })).build()).build();
  }
}

