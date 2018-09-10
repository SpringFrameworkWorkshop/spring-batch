package com.everis.alicante.spring.workshop.batch;

import com.everis.alicante.spring.workshop.batch.model.Transferencia;
import com.everis.alicante.spring.workshop.batch.model.TransferenciaXML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ListenersConfig {

  private static final Log logger = LogFactory.getLog(ListenersConfig.class);

  @Bean
  public ItemReadListener<Transferencia> itemReadListener() {
    return new ItemReadListener<Transferencia>() {
      @Override
      public void beforeRead() { }

      @Override
      public void afterRead(Transferencia o) {
        logger.info("ItemReadListener - afterRead: " + o.getBeneficiario());
      }

      @Override
      public void onReadError(Exception e) {
        logger.error("ItemReadListener - onReadError", e);
      }
    };
  }

  @Bean
  public ItemWriteListener<TransferenciaXML> itemWriteTransferenciaXMLListener() {
    return new ItemWriteListener<TransferenciaXML>() {
      @Override
      public void beforeWrite(List<? extends TransferenciaXML> list) { }

      @Override
      public void afterWrite(List<? extends TransferenciaXML> list) {
        logger.info("ItemWriteListener<TrasnferenciaXML> - afterWrite: "+list.stream().map(TransferenciaXML::getBeneficiario).collect(
          Collectors.joining(", ")));
      }

      @Override
      public void onWriteError(Exception e, List<? extends TransferenciaXML> list) {
        logger.error("ItemWriteListener<TrasnferenciaXML> - onWriteError", e);
      }
    };
  }

  @Bean
  public ItemWriteListener<Transferencia> itemWriteTransferenciaListener() {
    return new ItemWriteListener<Transferencia>() {
      @Override
      public void beforeWrite(List<? extends Transferencia> list) { }

      @Override
      public void afterWrite(List<? extends Transferencia> list) {
        logger.info("ItemWriteListener<Trasnferencia> - afterWrite: "+list.stream().map(Transferencia::getBeneficiario).collect(
          Collectors.joining(", ")));
      }

      @Override
      public void onWriteError(Exception e, List<? extends Transferencia> list) {
        logger.error("ItemWriteListener<Trasnferencia> - onWriteError", e);
      }
    };
  }

}
