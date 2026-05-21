package edu.prz.hopsops.transactions.application.transaction;

import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentTypeId;
import edu.prz.hopsops.shared.identity.SalesOfferId;
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
public class RegisterSaleTransactionUseCase {

  final TransactionFactory transactionFactory;
  final TransactionRepository transactionRepository;
//ma dodawac wiele tranzakcji, ale na razie niech dodaje jedna tranzakcje, potem bedziemy
// przykldka taska (dorobic usulge do tranzakcji) 
  @Transactional
  public Transaction execute(Command command) {
    Transaction transaction = transactionFactory.create(
        new TransactionFactory.Input(command.customerId(), command.transactionDate()));
    transaction.addSaleItem(
        command.salesOfferId(),
        command.equipmentTypeId(),
        command.quantity(),
        command.unitPrice()
    );
    transaction.register();
    return transactionRepository.save(transaction);
  }

  public record Command(
      CustomerId customerId,
      LocalDate transactionDate,
      SalesOfferId salesOfferId,
      EquipmentTypeId equipmentTypeId,
      Integer quantity,
      BigDecimal unitPrice
  ) {
  }
}
