package com.everis.alicante.spring.workshop.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ScheduleConfig {

  @Autowired
  private Job csvToXmlJob;

  @Autowired
  private JobLauncher jobLauncher;

  @Scheduled(fixedRate = 5000)
  public void launchCsvToXmlJob()
    throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
    JobInstanceAlreadyCompleteException {
    final JobParameters jobParameters = new JobParametersBuilder()
      .addLong("time",System.currentTimeMillis()).toJobParameters();
    jobLauncher.run(csvToXmlJob, jobParameters);
  }
}
