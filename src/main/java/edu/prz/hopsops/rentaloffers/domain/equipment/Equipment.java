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

  public static Equipment create(
      RentalOfferId rentalOfferId,
      String serialNumber,
      EquipmentCondition condition
  ) {
    if (rentalOfferId == null) {
      throw new IllegalArgumentException("Rental offer id is required");
    }
    if (serialNumber == null || serialNumber.isBlank()) {
      throw new IllegalArgumentException("Serial number is required");
    }

    Equipment equipment = new Equipment();
    equipment.rentalOfferId = rentalOfferId;
    equipment.serialNumber = serialNumber;
    equipment.condition = condition == null ? EquipmentCondition.GOOD : condition;
    equipment.status = EquipmentStatus.AVAILABLE;
    return equipment;
  }

  public void markRented() {
    if (status != EquipmentStatus.AVAILABLE && status != EquipmentStatus.RESERVED) {
      throw new IllegalStateException("Only available or reserved equipment can be rented");
    }
    status = EquipmentStatus.RENTED;
  }

  public void markReserved() {
    if (status != EquipmentStatus.AVAILABLE) {
      throw new IllegalStateException("Only available equipment can be reserved");
    }
    status = EquipmentStatus.RESERVED;
  }

  public void markAvailable() {
    status = EquipmentStatus.AVAILABLE;
  }

  public boolean isAvailable() {
    return status == EquipmentStatus.AVAILABLE;
  }

  public boolean isReserved() {
    return status == EquipmentStatus.RESERVED;
  }
}
