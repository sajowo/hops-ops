package edu.prz.hopsops.salesoffers.domain.salesoffer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
public class SalesOffer {

  @Id
  Long id;

  String equipmentName;

  String category;

  String manufacturer;

  String description;

  BigDecimal salePrice;

  Boolean active;
}
