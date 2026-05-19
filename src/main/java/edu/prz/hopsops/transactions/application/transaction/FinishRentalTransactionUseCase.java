package edu.prz.hopsops.transactions.application.transaction;

import edu.prz.hopsops.transactions.domain.transaction.Transaction;
import edu.prz.hopsops.transactions.domain.transaction.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinishRentalTransactionUseCase {

  final TransactionRepository transactionRepository;

  @Transactional
  public Transaction execute(Command command) {
    Transaction transaction = transactionRepository.findById(command.transactionId())
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    transaction.finishRental(command.rentalEndDate(), command.additionalFee());
    return transactionRepository.save(transaction);
  }

  public record Command(
      Long transactionId,
      LocalDate rentalEndDate,
      BigDecimal additionalFee
  ) {
  }
}
