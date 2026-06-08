package edu.prz.hopsops.transactions.domain.transaction;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import edu.prz.hopsops.shared.identity.EquipmentTypeId;
import edu.prz.hopsops.shared.identity.SalesOfferId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {

  @Enumerated(EnumType.STRING)
  TransactionType type;

  @Enumerated(EnumType.STRING)
  TransactionStatus status;

  @AttributeOverride(name = "id", column = @Column(name = "customer_id"))
  CustomerId customerId;

  LocalDate transactionDate;

  BigDecimal totalAmount = BigDecimal.ZERO;

  BigDecimal additionalFee = BigDecimal.ZERO;

  @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  List<TransactionItem> items = new ArrayList<>();

  public static Transaction create(CustomerId customerId, LocalDate transactionDate) {
    if (customerId == null) {
      throw new IllegalArgumentException("Customer id is required");
    }
    if (transactionDate == null) {
      throw new IllegalArgumentException("Transaction date is required");
    }

    Transaction transaction = new Transaction();
    transaction.customerId = customerId;
    transaction.transactionDate = transactionDate;
    transaction.type = TransactionType.EMPTY;
    transaction.status = TransactionStatus.DRAFT;
    return transaction;
  }

  public void addSaleItem(
      SalesOfferId salesOfferId,
      EquipmentTypeId equipmentTypeId,
      Integer quantity,
      BigDecimal unitPrice
  ) {
    ensureDraftOrInProgress();
    items.add(TransactionItem.sale(this, salesOfferId, equipmentTypeId, quantity, unitPrice));
    recalculate();
  }

  public void addRentalItem(
      EquipmentId equipmentId,
      LocalDate rentalStartDate,
      LocalDate plannedRentalEndDate,
      BigDecimal unitPrice
  ) {
    ensureDraftOrInProgress();
    items.add(TransactionItem.rental(
        this,
        equipmentId,
        rentalStartDate,
        plannedRentalEndDate,
        unitPrice
    ));
    recalculate();
  }

  public void register() {
    if (items.isEmpty()) {
      throw new IllegalStateException("Transaction must contain at least one item");
    }
    status = containsRentalItem() ? TransactionStatus.IN_PROGRESS : TransactionStatus.COMPLETED;
  }

  public void finishTransaction(LocalDate rentalEndDate, BigDecimal additionalFee) {
    if (!containsRentalItem()) {
      throw new IllegalStateException("Only rental transaction can be finished");
    }
    if (status != TransactionStatus.IN_PROGRESS) {
      throw new IllegalStateException("Only in-progress rental transaction can be finished");
    }
    if (rentalEndDate == null) {
      throw new IllegalArgumentException("Rental end date is required");
    }
    if (additionalFee != null && additionalFee.signum() < 0) {
      throw new IllegalArgumentException("Additional fee cannot be negative");
    }

    this.additionalFee = additionalFee == null ? BigDecimal.ZERO : additionalFee;
    items.stream()
        .filter(TransactionItem::isRental)
        .forEach(item -> item.finish(rentalEndDate));
    status = TransactionStatus.COMPLETED;
    recalculate();
  }

  public void cancel() {
    if (status == TransactionStatus.COMPLETED) {
      throw new IllegalStateException("Completed transaction cannot be cancelled");
    }
    status = TransactionStatus.CANCELLED;
  }

  public List<EquipmentId> rentalEquipmentIds() {
    return items.stream()
        .filter(TransactionItem::isRental)
        .map(TransactionItem::getEquipmentId)
        .toList();
  }

  private void ensureDraftOrInProgress() {
    if (status != TransactionStatus.DRAFT && status != TransactionStatus.IN_PROGRESS) {
      throw new IllegalStateException("Cannot change transaction in status: " + status);
    }
  }

  private boolean containsRentalItem() {
    return items.stream().anyMatch(TransactionItem::isRental);
  }

  private void recalculate() {
    totalAmount = items.stream()
        .map(TransactionItem::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .add(additionalFee);
    type = resolveType();
  }

  private TransactionType resolveType() {
    boolean sale = items.stream().anyMatch(TransactionItem::isSale);
    boolean rental = items.stream().anyMatch(TransactionItem::isRental);

    if (sale && rental) {
      return TransactionType.MIXED;
    }
    if (sale) {
      return TransactionType.SALE;
    }
    if (rental) {
      return TransactionType.RENTAL;
    }
    return TransactionType.EMPTY;
  }
}
