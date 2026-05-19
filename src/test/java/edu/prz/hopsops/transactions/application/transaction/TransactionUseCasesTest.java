package edu.prz.hopsops.transactions.application.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentItemId;
import edu.prz.hopsops.shared.identity.EquipmentTypeId;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import edu.prz.hopsops.shared.identity.SalesOfferId;
import edu.prz.hopsops.transactions.application.transaction.FinishRentalTransactionUseCase.Command;
import edu.prz.hopsops.transactions.domain.transaction.Transaction;
import edu.prz.hopsops.transactions.domain.transaction.TransactionRepository;
import edu.prz.hopsops.transactions.domain.transaction.TransactionStatus;
import edu.prz.hopsops.transactions.domain.transaction.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:transactions-test;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class TransactionUseCasesTest {

  @Autowired
  RegisterSaleTransactionUseCase registerSaleTransactionUseCase;

  @Autowired
  RegisterRentalTransactionUseCase registerRentalTransactionUseCase;

  @Autowired
  FinishRentalTransactionUseCase finishRentalTransactionUseCase;

  @Autowired
  TransactionRepository transactionRepository;

  @BeforeEach
  void setUp() {
    transactionRepository.deleteAll();
  }

  @Test
  void shouldRegisterSaleTransaction() {
    Transaction transaction = registerSaleTransactionUseCase.execute(
        new RegisterSaleTransactionUseCase.Command(
            new CustomerId(1L),
            LocalDate.of(2026, 5, 20),
            new SalesOfferId(10L),
            new EquipmentTypeId(20L),
            2,
            new BigDecimal("100.00")
        )
    );

    assertNotNull(transaction.getId());
    assertEquals(TransactionType.SALE, transaction.getType());
    assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
    assertEquals(new BigDecimal("200.00"), transaction.getTotalAmount());
    assertEquals(1, transaction.getItems().size());
    assertEquals(1, transactionRepository.count());
  }

  @Test
  void shouldRegisterAndFinishRentalTransaction() {
    Transaction rental = registerRentalTransactionUseCase.execute(
        new RegisterRentalTransactionUseCase.Command(
            new CustomerId(1L),
            LocalDate.of(2026, 5, 20),
            new RentalOfferId(30L),
            new EquipmentItemId(40L),
            LocalDate.of(2026, 5, 20),
            LocalDate.of(2026, 5, 23),
            new BigDecimal("50.00")
        )
    );

    assertEquals(TransactionType.RENTAL, rental.getType());
    assertEquals(TransactionStatus.IN_PROGRESS, rental.getStatus());
    assertEquals(new BigDecimal("50.00"), rental.getTotalAmount());

    Transaction finished = finishRentalTransactionUseCase.execute(
        new Command(
            rental.getId(),
            LocalDate.of(2026, 5, 22),
            new BigDecimal("15.00")
        )
    );

    assertEquals(TransactionStatus.COMPLETED, finished.getStatus());
    assertEquals(new BigDecimal("65.00"), finished.getTotalAmount());
    assertEquals(LocalDate.of(2026, 5, 22), finished.getItems().getFirst().getRentalEndDate());
  }
}
