package edu.prz.hopsops.transactions.application.transaction;

import edu.prz.hopsops.customers.domain.customer.CustomerRepository;
import edu.prz.hopsops.foundation.domain.NotExistsException;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOffer;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOfferRepository;
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
  final SalesOfferRepository salesOfferRepository;
  final CustomerRepository customerRepository;

  @Transactional
  public Transaction execute(Command command) {
    ensureCustomerExists(command.customerId());
    SalesOffer salesOffer = salesOfferRepository.findById(command.salesOfferId().id())
        .orElseThrow(() -> NotExistsException.of("Sales offer not found"));
    BigDecimal unitPrice = salesOffer.resolveUnitPrice(command.unitPrice());

    Transaction transaction = transactionFactory.create(
        new TransactionFactory.Input(command.customerId(), command.transactionDate()));
    transaction.addSaleItem(
        command.salesOfferId(),
        command.equipmentTypeId(),
        command.quantity(),
        unitPrice
    );
    transaction.register();
    return transactionRepository.save(transaction);
  }

  private void ensureCustomerExists(CustomerId customerId) {
    if (!customerRepository.existsById(customerId.id())) {
      throw NotExistsException.of("Customer not found");
    }
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
