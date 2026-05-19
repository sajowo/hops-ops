package edu.prz.hopsops.transactions.domain.transaction;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

@Entity
@Data
public class Transaction {

  @Id
  Long id;

  @Enumerated(EnumType.STRING)
  TransationType type;

  BigDecimal totalAmount;

  CustomerId customerId;

  @OneToMany
  List<RentalItem> rentalItemList;

  @OneToMany
  List<SaleItem> saleItemList;
}
