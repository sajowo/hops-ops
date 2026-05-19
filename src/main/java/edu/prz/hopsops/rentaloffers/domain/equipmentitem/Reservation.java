package edu.prz.hopsops.rentaloffers.domain.equipmentitem;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import edu.prz.hopsops.shared.identity.CustomerId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "equipmentItem", callSuper = true)
public class Reservation extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "equipment_item_id", referencedColumnName = "id", nullable = false)
  EquipmentItem equipmentItem;

  @AttributeOverride(name = "id", column = @Column(name = "customer_id"))
  CustomerId customerId;

  LocalDate reservedFrom;

  LocalDate reservedTo;

  static Reservation create(
      EquipmentItem equipmentItem,
      CustomerId customerId,
      LocalDate reservedFrom,
      LocalDate reservedTo
  ) {
    if (equipmentItem == null) {
      throw new IllegalArgumentException("Equipment item is required");
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
    reservation.equipmentItem = equipmentItem;
    reservation.customerId = customerId;
    reservation.reservedFrom = reservedFrom;
    reservation.reservedTo = reservedTo;
    return reservation;
  }
}
