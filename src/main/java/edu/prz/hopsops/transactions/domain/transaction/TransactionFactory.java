package edu.prz.hopsops.transactions.domain.transaction;

import edu.prz.hopsops.foundation.domain.StandardFactory;
import edu.prz.hopsops.shared.identity.CustomerId;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class TransactionFactory implements StandardFactory<TransactionFactory.Input, Transaction> {

  public record Input(CustomerId customerId, LocalDate transactionDate) {

  }

  @Override
  public Transaction create(Input input) {
    return Transaction.create(input.customerId(), input.transactionDate());
  }
}
