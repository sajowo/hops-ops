package edu.prz.hopsops.transactions.domain.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
public class Transaction {

  @Id
  Long id;

  @Enumerated(EnumType.STRING)
  TransationType type;

  BigDecimal totalAmount;
}
