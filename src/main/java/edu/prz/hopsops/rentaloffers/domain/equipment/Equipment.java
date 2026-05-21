package edu.prz.hopsops.rentaloffers.domain.equipment;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Equipment extends BaseEntity {

  @AttributeOverride(name = "id", column = @Column(name = "rental_offer_id"))
  RentalOfferId rentalOfferId;

  String serialNumber;

  @Enumerated(EnumType.STRING)
  EquipmentStatus status;

  @Enumerated(EnumType.STRING)
  EquipmentCondition condition;

  public void markRented() {
    status = EquipmentStatus.RENTED;
  }

  public void markAvailable() {
    status = EquipmentStatus.AVAILABLE;
  }
}
