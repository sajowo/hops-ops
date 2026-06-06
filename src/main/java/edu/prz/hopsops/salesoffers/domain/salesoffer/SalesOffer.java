package edu.prz.hopsops.salesoffers.domain.salesoffer;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOffer extends BaseEntity {

  String equipmentName;

  String category;

  String manufacturer;

  String description;

  BigDecimal salePrice;

  Boolean active;

  public static SalesOffer create(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal salePrice
  ) {
    SalesOffer offer = new SalesOffer();
    offer.applyDetails(equipmentName, category, manufacturer, description, salePrice);
    offer.active = true;
    return offer;
  }

  public void updateDetails(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal salePrice
  ) {
    applyDetails(equipmentName, category, manufacturer, description, salePrice);
  }

  public void activate() {
    active = true;
  }

  public void deactivate() {
    active = false;
  }

  public BigDecimal resolveUnitPrice(BigDecimal requestedUnitPrice) {
    if (!Boolean.TRUE.equals(active)) {
      throw new IllegalStateException("Sales offer is inactive");
    }
    if (requestedUnitPrice != null) {
      ensureNonNegative(requestedUnitPrice, "Unit price cannot be negative");
      return requestedUnitPrice;
    }
    return salePrice;
  }

  private void applyDetails(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal salePrice
  ) {
    if (equipmentName == null || equipmentName.isBlank()) {
      throw new IllegalArgumentException("Equipment name is required");
    }
    ensureNonNegative(salePrice, "Sale price cannot be negative");
    this.equipmentName = equipmentName;
    this.category = category;
    this.manufacturer = manufacturer;
    this.description = description;
    this.salePrice = salePrice;
  }

  private void ensureNonNegative(BigDecimal value, String message) {
    if (value == null || value.signum() < 0) {
      throw new IllegalArgumentException(message);
    }
  }
}
