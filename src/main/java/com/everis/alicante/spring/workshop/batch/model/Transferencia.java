package com.everis.alicante.spring.workshop.batch.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement(name = "transferencia")
public class Transferencia {
  private String beneficiario;
  private Date fecha;
  private Double importe;
  private String concepto;
}