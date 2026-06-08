package edu.prz.hopsops.transactions.application.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.prz.hopsops.customers.application.customer.CreateCustomerUseCase;
import edu.prz.hopsops.customers.domain.customer.Customer;
import edu.prz.hopsops.customers.domain.customer.CustomerRepository;
import edu.prz.hopsops.rentaloffers.application.rentaloffer.AddEquipmentToRentalOfferUseCase;
import edu.prz.hopsops.rentaloffers.application.rentaloffer.CreateRentalOfferUseCase;
import edu.prz.hopsops.rentaloffers.domain.equipment.Equipment;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentCondition;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentRepository;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentStatus;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOffer;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferRepository;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferStatus;
import edu.prz.hopsops.salesoffers.application.salesoffer.CreateSalesOfferUseCase;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOffer;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOfferRepository;
import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import edu.prz.hopsops.shared.identity.EquipmentTypeId;
import edu.prz.hopsops.shared.identity.SalesOfferId;
import edu.prz.hopsops.transactions.application.transaction.FinishTransactionUseCase.Command;
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
  FinishTransactionUseCase finishTransactionUseCase;

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  CreateSalesOfferUseCase createSalesOfferUseCase;

  @Autowired
  SalesOfferRepository salesOfferRepository;

  @Autowired
  CreateRentalOfferUseCase createRentalOfferUseCase;

  @Autowired
  AddEquipmentToRentalOfferUseCase addEquipmentToRentalOfferUseCase;

  @Autowired
  RentalOfferRepository rentalOfferRepository;

  @Autowired
  EquipmentRepository equipmentRepository;

  @Autowired
  CreateCustomerUseCase createCustomerUseCase;

  @Autowired
  CustomerRepository customerRepository;

  @BeforeEach
  void setUp() {
    transactionRepository.deleteAll();
    equipmentRepository.deleteAll();
    rentalOfferRepository.deleteAll();
    salesOfferRepository.deleteAll();
    customerRepository.deleteAll();
  }

  @Test
  void shouldRegisterSaleTransaction() {
    Customer customer = createCustomer();
    SalesOffer salesOffer = createSalesOfferUseCase.execute(
        new CreateSalesOfferUseCase.Command(
            "Wiertarka",
            "Narzędzia",
            "Makita",
            "Wiertarka udarowa",
            new BigDecimal("100.00")
        )
    );

    Transaction transaction = registerSaleTransactionUseCase.execute(
        new RegisterSaleTransactionUseCase.Command(
            new CustomerId(customer.getId()),
            LocalDate.of(2026, 5, 20),
            new SalesOfferId(salesOffer.getId()),
            new EquipmentTypeId(20L),
            2,
            null
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
  void shouldRegisterAndFinishTransaction() {
    Customer customer = createCustomer();
    RentalOffer rentalOffer = createRentalOfferUseCase.execute(
        new CreateRentalOfferUseCase.Command(
            "Koparka",
            "Budowlane",
            "CAT",
            "Mała koparka",
            new BigDecimal("50.00")
        )
    );
    Equipment equipment = addEquipmentToRentalOfferUseCase.execute(
        new AddEquipmentToRentalOfferUseCase.Command(
            rentalOffer.getId(),
            "CAT-001",
            EquipmentCondition.GOOD
        )
    );

    Transaction rental = registerRentalTransactionUseCase.execute(
        new RegisterRentalTransactionUseCase.Command(
            new CustomerId(customer.getId()),
            LocalDate.of(2026, 5, 20),
            new EquipmentId(equipment.getId()),
            LocalDate.of(2026, 5, 20),
            LocalDate.of(2026, 5, 23),
            null
        )
    );

    assertEquals(TransactionType.RENTAL, rental.getType());
    assertEquals(TransactionStatus.IN_PROGRESS, rental.getStatus());
    assertEquals(new BigDecimal("50.00"), rental.getTotalAmount());
    assertEquals(EquipmentStatus.RENTED, equipmentRepository.findById(equipment.getId())
        .orElseThrow()
        .getStatus());
    assertEquals(RentalOfferStatus.RENTED, rentalOfferRepository.findById(rentalOffer.getId())
        .orElseThrow()
        .getStatus());

    Transaction finished = finishTransactionUseCase.execute(
        new Command(
            rental.getId(),
            LocalDate.of(2026, 5, 22),
            new BigDecimal("15.00")
        )
    );

    assertEquals(TransactionStatus.COMPLETED, finished.getStatus());
    assertEquals(new BigDecimal("65.00"), finished.getTotalAmount());
    assertEquals(LocalDate.of(2026, 5, 22), finished.getItems().getFirst().getRentalEndDate());
    assertEquals(EquipmentStatus.AVAILABLE, equipmentRepository.findById(equipment.getId())
        .orElseThrow()
        .getStatus());
    assertEquals(RentalOfferStatus.AVAILABLE, rentalOfferRepository.findById(rentalOffer.getId())
        .orElseThrow()
        .getStatus());
  }

  private Customer createCustomer() {
    return createCustomerUseCase.execute(
        new CreateCustomerUseCase.Command(
            "Jan",
            "Kowalski",
            "Rzeszów",
            "500600700",
            "jan.kowalski@example.com"
        )
    );
  }
}
