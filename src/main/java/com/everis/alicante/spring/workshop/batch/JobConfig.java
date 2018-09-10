package com.everis.alicante.spring.workshop.batch;

import com.everis.alicante.spring.workshop.batch.model.Transferencia;
import com.everis.alicante.spring.workshop.batch.model.TransferenciaXML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;
import java.beans.PropertyEditor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JobConfig {

  @Autowired
  private StepBuilderFactory sbf;

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
  public ItemProcessor<Transferencia, TransferenciaXML> processor(DateFormat sdfEspanol, Double cambioEuroDollar) {
    return transferencia -> {
      final TransferenciaXML xml = new TransferenciaXML();
      xml.setBeneficiario(transferencia.getBeneficiario());
      xml.setConcepto(transferencia.getConcepto());
      xml.setFechaEspanola(sdfEspanol.format(transferencia.getFecha()));
      xml.setImporteDolares(transferencia.getImporte() * cambioEuroDollar);
      return xml;
    };
  }

  @Bean
  public StaxEventItemWriter<TransferenciaXML> writer(@Value("file:output/data.xml") Resource output) {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setClassesToBeBound(TransferenciaXML.class);
    return new StaxEventItemWriterBuilder<TransferenciaXML>()
      .name("writer")
      .rootTagName("transferencias")
      .resource(output)
      .marshaller(marshaller)
      .build();
  }

  @Bean
  public JdbcBatchItemWriter<Transferencia> jdbcBatchItemWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Transferencia>()
      .dataSource(dataSource)
      .sql("insert into transferencias (beneficiario, fecha, importe, concepto) values (:beneficiario, :fecha, :importe, :concepto)")
      .beanMapped()
      .build();
  }

  @Bean
  public Step csvToXmlStep(FlatFileItemReader<Transferencia> reader,
    ItemProcessor<Transferencia,TransferenciaXML> processor, StaxEventItemWriter<TransferenciaXML> writer,
    ItemReadListener<Transferencia> itemReadListener,
    ItemWriteListener<TransferenciaXML> itemWriteTransferenciaXMLListener) {
    return sbf.get("csvToXmlStep").<Transferencia, TransferenciaXML>chunk(10)
      .reader(reader)
      .processor(processor)
      .writer(writer)
      .listener(itemReadListener)
      .listener(itemWriteTransferenciaXMLListener)
      .build();
  }

  @Bean
  public Step csvToDatabaseStep(FlatFileItemReader<Transferencia> reader,
    JdbcBatchItemWriter<Transferencia> writer,
    ItemReadListener<Transferencia> itemReadListener,
    ItemWriteListener<Transferencia> itemWriteTransferenciaListener) {
    return sbf.get("csvToDatabaseStep").<Transferencia, Transferencia>chunk(10)
      .reader(reader)
      .writer(writer)
      .listener(itemReadListener)
      .listener(itemWriteTransferenciaListener)
      .build();
  }

  @Bean
  public Job csvToXmlJob(JobBuilderFactory jobBuilderFactory, Step csvToXmlStep, Step csvToDatabaseStep) {
    return jobBuilderFactory.get("csvToXmlJob")
      .incrementer(new RunIdIncrementer())
      .start(csvToXmlStep)
      .next(csvToDatabaseStep)
      .build();
  }

}
