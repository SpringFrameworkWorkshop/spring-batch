package com.everis.alicante.spring.workshop.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class GeneralConfig {

  @Bean
  public SimpleDateFormat sdfEspanol() {
    return new SimpleDateFormat("dd/MM/yyyy 'del Año del Señor'");
  }

  @Bean
  public Double cambioEuroDollar() {
    return 1.15;
  }


}
