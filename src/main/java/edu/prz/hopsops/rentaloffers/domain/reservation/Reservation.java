package edu.prz.hopsops.rentaloffers.domain.reservation;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Reservation extends BaseEntity {

  @AttributeOverride(name = "id", column = @Column(name = "equipment_id"))
  EquipmentId equipmentId;

  @AttributeOverride(name = "id", column = @Column(name = "customer_id"))
  CustomerId customerId;

  LocalDate reservedFrom;

  LocalDate reservedTo;

  public static Reservation create(
      EquipmentId equipmentId,
      CustomerId customerId,
      LocalDate reservedFrom,
      LocalDate reservedTo
  ) {
    if (equipmentId == null) {
      throw new IllegalArgumentException("Equipment id is required");
    }
    if (customerId == null) {
      throw new IllegalArgumentException("Customer id is required");
    }
    if (reservedFrom == null) {
      throw new IllegalArgumentException("Reservation start date is required");
    }
    if (reservedTo == null || reservedTo.isBefore(reservedFrom)) {
      throw new IllegalArgumentException("Reservation end date cannot be before start date");
    }

    Reservation reservation = new Reservation();
    reservation.equipmentId = equipmentId;
    reservation.customerId = customerId;
    reservation.reservedFrom = reservedFrom;
    reservation.reservedTo = reservedTo;
    return reservation;
  }
}
