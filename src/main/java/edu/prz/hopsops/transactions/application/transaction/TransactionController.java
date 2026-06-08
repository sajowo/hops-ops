package edu.prz.hopsops.transactions.application.transaction;

import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import edu.prz.hopsops.shared.identity.EquipmentTypeId;
import edu.prz.hopsops.shared.identity.SalesOfferId;
import edu.prz.hopsops.transactions.domain.transaction.Transaction;
import edu.prz.hopsops.transactions.domain.transaction.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

  final TransactionRepository transactionRepository;
  final RegisterSaleTransactionUseCase registerSaleTransactionUseCase;
  final RegisterRentalTransactionUseCase registerRentalTransactionUseCase;
  final FinishTransactionUseCase finishTransactionUseCase;

  @PostMapping("/sales")
  public ResponseEntity<Transaction> registerSale(@RequestBody RegisterSaleRequest request) {
    return ResponseEntity.ok(registerSaleTransactionUseCase.execute(request.toCommand()));
  }

  @PostMapping("/rentals")
  public ResponseEntity<Transaction> registerRental(@RequestBody RegisterRentalRequest request) {
    return ResponseEntity.ok(registerRentalTransactionUseCase.execute(request.toCommand()));
  }

  @PostMapping("/{id}/finish")
  public ResponseEntity<Transaction> finish(
      @PathVariable Long id,
      @RequestBody FinishTransactionRequest request
  ) {
    return ResponseEntity.ok(finishTransactionUseCase.execute(request.toCommand(id)));
  }

  @GetMapping
  public ResponseEntity<List<Transaction>> getAll() {
    return ResponseEntity.ok(transactionRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Transaction> getOne(@PathVariable Long id) {
    return transactionRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  public record RegisterSaleRequest(
      Long customerId,
      LocalDate transactionDate,
      Long salesOfferId,
      Long equipmentTypeId,
      Integer quantity,
      BigDecimal unitPrice
  ) {

    RegisterSaleTransactionUseCase.Command toCommand() {
      return new RegisterSaleTransactionUseCase.Command(
          new CustomerId(customerId),
          transactionDate,
          new SalesOfferId(salesOfferId),
          new EquipmentTypeId(equipmentTypeId),
          quantity,
          unitPrice
      );
    }
  }

  public record RegisterRentalRequest(
      Long customerId,
      LocalDate transactionDate,
      Long equipmentId,
      LocalDate rentalStartDate,
      LocalDate plannedRentalEndDate,
      BigDecimal unitPrice
  ) {

    RegisterRentalTransactionUseCase.Command toCommand() {
      return new RegisterRentalTransactionUseCase.Command(
          new CustomerId(customerId),
          transactionDate,
          new EquipmentId(equipmentId),
          rentalStartDate,
          plannedRentalEndDate,
          unitPrice
      );
    }
  }

  public record FinishTransactionRequest(
      LocalDate finishedAt,
      BigDecimal additionalFee
  ) {

    FinishTransactionUseCase.Command toCommand(Long transactionId) {
      return new FinishTransactionUseCase.Command(transactionId, finishedAt, additionalFee);
    }
  }
}
