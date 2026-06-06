package edu.prz.hopsops.rentaloffers.domain.rentaloffer;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class RentalOffer extends BaseEntity {

  String equipmentName;

  String category;

  String manufacturer;

  String description;

  BigDecimal rentalPrice;

  Integer availableItems;

  @Enumerated(EnumType.STRING)
  RentalOfferStatus status;

  public static RentalOffer create(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal rentalPrice
  ) {
    RentalOffer offer = new RentalOffer();
    offer.availableItems = 0;
    offer.status = RentalOfferStatus.UNAVAILABLE;
    offer.applyDetails(equipmentName, category, manufacturer, description, rentalPrice);
    return offer;
  }

  public void updateDetails(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal rentalPrice
  ) {
    applyDetails(equipmentName, category, manufacturer, description, rentalPrice);
  }

  public void registerEquipmentItem() {
    availableItems = currentAvailableItems() + 1;
    status = RentalOfferStatus.AVAILABLE;
  }

  public void markItemReserved() {
    ensureAvailableItem();
    availableItems = currentAvailableItems() - 1;
    status = availableItems > 0 ? RentalOfferStatus.AVAILABLE : RentalOfferStatus.RESERVED;
  }

  public void markItemRented() {
    ensureAvailableItem();
    availableItems = currentAvailableItems() - 1;
    status = availableItems > 0 ? RentalOfferStatus.AVAILABLE : RentalOfferStatus.RENTED;
  }

  public void markReservedItemRented() {
    status = currentAvailableItems() > 0 ? RentalOfferStatus.AVAILABLE : RentalOfferStatus.RENTED;
  }

  public void markItemReturned() {
    availableItems = currentAvailableItems() + 1;
    status = RentalOfferStatus.AVAILABLE;
  }

  public BigDecimal resolveUnitPrice(BigDecimal requestedUnitPrice) {
    if (requestedUnitPrice != null) {
      ensureNonNegative(requestedUnitPrice, "Unit price cannot be negative");
      return requestedUnitPrice;
    }
    return rentalPrice;
  }

  private void applyDetails(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal rentalPrice
  ) {
    if (equipmentName == null || equipmentName.isBlank()) {
      throw new IllegalArgumentException("Equipment name is required");
    }
    ensureNonNegative(rentalPrice, "Rental price cannot be negative");
    this.equipmentName = equipmentName;
    this.category = category;
    this.manufacturer = manufacturer;
    this.description = description;
    this.rentalPrice = rentalPrice;
  }

  private void ensureAvailableItem() {
    if (currentAvailableItems() <= 0) {
      throw new IllegalStateException("Rental offer has no available equipment");
    }
  }

  private int currentAvailableItems() {
    return availableItems == null ? 0 : availableItems;
  }

  private void ensureNonNegative(BigDecimal value, String message) {
    if (value == null || value.signum() < 0) {
      throw new IllegalArgumentException(message);
    }
  }
}
