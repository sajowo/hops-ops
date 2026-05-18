package edu.prz.hopsops.rentaloffers.domain.rentaloffer;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
public class RentalOffer {

  @Id
  Long id;

  String equipmentName;

  String category;

  String manufacturer;

  String description;

  BigDecimal rentalPrice;

  Integer availableItems;

  @Enumerated(EnumType.STRING)
  RentalOfferStatus status;
}
