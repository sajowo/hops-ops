package edu.prz.hopsops.transactions.application.transaction;

import edu.prz.hopsops.rentaloffers.domain.equipment.Equipment;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentRepository;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOffer;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferRepository;
import edu.prz.hopsops.shared.identity.EquipmentId;
import edu.prz.hopsops.transactions.domain.transaction.Transaction;
import edu.prz.hopsops.transactions.domain.transaction.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinishTransactionUseCase {

  final TransactionRepository transactionRepository;
  final EquipmentRepository equipmentRepository;
  final RentalOfferRepository rentalOfferRepository;

  @Transactional
  public Transaction execute(Command command) {
    Transaction transaction = transactionRepository.findById(command.transactionId())
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    List<EquipmentId> rentedEquipmentIds = transaction.rentalEquipmentIds();
    transaction.finishTransaction(command.finishedAt(), command.additionalFee());
    rentedEquipmentIds.forEach(this::releaseEquipment);
    return transactionRepository.save(transaction);
  }

  private void releaseEquipment(EquipmentId equipmentId) {
    Equipment equipment = equipmentRepository.findById(equipmentId.id())
        .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));
    RentalOffer rentalOffer = rentalOfferRepository.findById(equipment.getRentalOfferId().id())
        .orElseThrow(() -> new IllegalArgumentException("Rental offer not found"));

    equipment.markAvailable();
    rentalOffer.markItemReturned();
  }

  public record Command(
      Long transactionId,
      LocalDate finishedAt,
      BigDecimal additionalFee
  ) {
  }
}
