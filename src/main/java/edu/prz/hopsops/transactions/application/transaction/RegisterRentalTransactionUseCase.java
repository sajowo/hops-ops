package edu.prz.hopsops.transactions.application.transaction;

import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import edu.prz.hopsops.transactions.domain.transaction.Transaction;
import edu.prz.hopsops.transactions.domain.transaction.TransactionFactory;
import edu.prz.hopsops.transactions.domain.transaction.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterRentalTransactionUseCase {

  final TransactionFactory transactionFactory;
  final TransactionRepository transactionRepository;

  @Transactional
  public Transaction execute(Command command) {
    Transaction transaction = transactionFactory.create(
        new TransactionFactory.Input(command.customerId(), command.transactionDate()));
    transaction.addRentalItem(
        command.equipmentId(),
        command.rentalStartDate(),
        command.plannedRentalEndDate(),
        command.unitPrice()
    );
    transaction.register();
    return transactionRepository.save(transaction);
  }

  public record Command(
      CustomerId customerId,
      LocalDate transactionDate,
      EquipmentId equipmentId,
      LocalDate rentalStartDate,
      LocalDate plannedRentalEndDate,
      BigDecimal unitPrice
  ) {
  }
}
