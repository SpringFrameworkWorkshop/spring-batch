package com.everis.alicante.spring.workshop.batch.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement(name = "transferencia")
public class TransferenciaXML {
  private String beneficiario;
  private String fechaEspanola;
  private Double importeDolares;
  private String concepto;
}
