package edu.prz.hopsops.rentaloffers.domain.equipmentitem;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class EquipmentItem extends BaseEntity {

  @AttributeOverride(name = "id", column = @Column(name = "rental_offer_id"))
  RentalOfferId rentalOfferId;

  String serialNumber;

  @Enumerated(EnumType.STRING)
  EquipmentItemStatus status;

  @Enumerated(EnumType.STRING)
  EquipmentCondition condition;

  @OneToMany(mappedBy = "equipmentItem", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  List<Reservation> reservations = new ArrayList<>();

  public void reserve(CustomerId customerId, LocalDate reservedFrom, LocalDate reservedTo) {
    if (status == EquipmentItemStatus.RENTED) {
      throw new IllegalStateException("Rented equipment item cannot be reserved");
    }

    reservations.add(Reservation.create(this, customerId, reservedFrom, reservedTo));
    status = EquipmentItemStatus.RESERVED;
  }

  public void markRented() {
    status = EquipmentItemStatus.RENTED;
  }

  public void markAvailable() {
    status = EquipmentItemStatus.AVAILABLE;
  }
}

//zmienic nazwe na equipment
