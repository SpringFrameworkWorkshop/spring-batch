package com.everis.alicante.spring.workshop.batch;

import com.everis.alicante.spring.workshop.batch.model.Transferencia;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class JobConfig {

  private static final Log logger = LogFactory.getLog(JobConfig.class);

  @Bean
  public FlatFileItemReader<Transferencia> reader(@Value("input/data.csv")Resource input) {
    return new FlatFileItemReaderBuilder<Transferencia>()
      .name("reader")
      .resource(input)
      .targetType(Transferencia.class)
      .linesToSkip(1)
      .delimited().delimiter(";").names(new String[]{"beneficiario", "fecha", "importe", "concepto"})
      .build();
  }

  @Bean
  public StaxEventItemWriter<Transferencia> writer(@Value("file:output/data.xml") Resource output) {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setClassesToBeBound(Transferencia.class);
    return new StaxEventItemWriterBuilder<Transferencia>()
      .name("writer")
      .rootTagName("transferencias")
      .resource(output)
      .marshaller(marshaller)
      .build();
  }

  @Bean
  public Step csvToXmlStep(StepBuilderFactory stepBuilderFactory,
    FlatFileItemReader<Transferencia> reader, StaxEventItemWriter<Transferencia> writer) {
    return stepBuilderFactory.get("csvToXmlStep")
      .<Transferencia, Transferencia>chunk(10)
      .reader(reader)
      .writer(writer)
      .build();
  }
  @Bean
  public Job csvToXmlJob(JobBuilderFactory jobBuilderFactory, Step csvToXmlStep) {
    return jobBuilderFactory.get("csvToXmlJob")
      .start(csvToXmlStep)
      .build();
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
