package edu.prz.hopsops.transactions.domain.transaction;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import edu.prz.hopsops.shared.identity.EquipmentItemId;
import edu.prz.hopsops.shared.identity.EquipmentTypeId;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import edu.prz.hopsops.shared.identity.SalesOfferId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "transaction", callSuper = true)
public class TransactionItem extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "transaction_id", referencedColumnName = "id", nullable = false)
  Transaction transaction;

  @Enumerated(EnumType.STRING)
  TransactionItemType type;

  @AttributeOverride(name = "id", column = @Column(name = "sales_offer_id"))
  SalesOfferId salesOfferId;

  @AttributeOverride(name = "id", column = @Column(name = "rental_offer_id"))
  RentalOfferId rentalOfferId;

  @AttributeOverride(name = "id", column = @Column(name = "equipment_type_id"))
  EquipmentTypeId equipmentTypeId;

  @AttributeOverride(name = "id", column = @Column(name = "equipment_item_id"))
  EquipmentItemId equipmentItemId;

  Integer quantity;

  BigDecimal unitPrice;

  BigDecimal totalPrice;

  LocalDate rentalStartDate;

  LocalDate plannedRentalEndDate;

  LocalDate rentalEndDate;

  static TransactionItem sale(
      Transaction transaction,
      SalesOfferId salesOfferId,
      EquipmentTypeId equipmentTypeId,
      Integer quantity,
      BigDecimal unitPrice
  ) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction is required");
    }
    if (salesOfferId == null) {
      throw new IllegalArgumentException("Sales offer id is required");
    }
    if (equipmentTypeId == null) {
      throw new IllegalArgumentException("Equipment type id is required");
    }
    if (quantity == null || quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }
    if (unitPrice == null || unitPrice.signum() < 0) {
      throw new IllegalArgumentException("Unit price cannot be negative");
    }

    TransactionItem item = new TransactionItem();
    item.transaction = transaction;
    item.type = TransactionItemType.SALE;
    item.salesOfferId = salesOfferId;
    item.equipmentTypeId = equipmentTypeId;
    item.quantity = quantity;
    item.unitPrice = unitPrice;
    item.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    return item;
  }

  static TransactionItem rental(
      Transaction transaction,
      RentalOfferId rentalOfferId,
      EquipmentItemId equipmentItemId,
      LocalDate rentalStartDate,
      LocalDate plannedRentalEndDate,
      BigDecimal unitPrice
  ) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction is required");
    }
    if (rentalOfferId == null) {
      throw new IllegalArgumentException("Rental offer id is required");
    }
    if (equipmentItemId == null) {
      throw new IllegalArgumentException("Equipment item id is required");
    }
    if (rentalStartDate == null) {
      throw new IllegalArgumentException("Rental start date is required");
    }
    if (plannedRentalEndDate == null || plannedRentalEndDate.isBefore(rentalStartDate)) {
      throw new IllegalArgumentException("Planned rental end date cannot be before start date");
    }
    if (unitPrice == null || unitPrice.signum() < 0) {
      throw new IllegalArgumentException("Unit price cannot be negative");
    }

    TransactionItem item = new TransactionItem();
    item.transaction = transaction;
    item.type = TransactionItemType.RENTAL;
    item.rentalOfferId = rentalOfferId;
    item.equipmentItemId = equipmentItemId;
    item.quantity = 1;
    item.unitPrice = unitPrice;
    item.totalPrice = unitPrice;
    item.rentalStartDate = rentalStartDate;
    item.plannedRentalEndDate = plannedRentalEndDate;
    return item;
  }

  boolean isSale() {
    return type == TransactionItemType.SALE;
  }

  boolean isRental() {
    return type == TransactionItemType.RENTAL;
  }

  void finishRental(LocalDate rentalEndDate) {
    if (!isRental()) {
      throw new IllegalStateException("Only rental item can be finished");
    }
    if (rentalEndDate.isBefore(rentalStartDate)) {
      throw new IllegalArgumentException("Rental end date cannot be before start date");
    }
    this.rentalEndDate = rentalEndDate;
  }
}
