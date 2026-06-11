package edu.prz.hopsops.transactions.application.transaction;

import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import edu.prz.hopsops.shared.identity.EquipmentTypeId;
import edu.prz.hopsops.shared.identity.SalesOfferId;
import edu.prz.hopsops.foundation.application.BaseController;
import edu.prz.hopsops.transactions.domain.transaction.Transaction;
import edu.prz.hopsops.transactions.domain.transaction.TransactionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "transactions", description = "Transactions")
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController extends BaseController {

  final TransactionRepository transactionRepository;
  final RegisterSaleTransactionUseCase registerSaleTransactionUseCase;
  final RegisterRentalTransactionUseCase registerRentalTransactionUseCase;
  final FinishTransactionUseCase finishTransactionUseCase;

  @PostMapping("/sales")
  public ResponseEntity<Transaction> registerSale(@RequestBody @Valid RegisterSaleRequest request) {
    return ResponseEntity.ok(registerSaleTransactionUseCase.execute(request.toCommand()));
  }

  @PostMapping("/rentals")
  public ResponseEntity<Transaction> registerRental(@RequestBody @Valid RegisterRentalRequest request) {
    return ResponseEntity.ok(registerRentalTransactionUseCase.execute(request.toCommand()));
  }

  @PostMapping(_ID + "/finish")
  public ResponseEntity<Transaction> finish(
      @PathVariable Long id,
      @RequestBody @Valid FinishTransactionRequest request
  ) {
    return ResponseEntity.ok(finishTransactionUseCase.execute(request.toCommand(id)));
  }

  @GetMapping
  public ResponseEntity<Page<Transaction>> getAll(Pageable pageable) {
    return ResponseEntity.ok(transactionRepository.findAll(pageable));
  }

  @GetMapping(_ID)
  public ResponseEntity<Transaction> getOne(@PathVariable Long id) {
    return transactionRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  public record RegisterSaleRequest(
      @NotNull
      @Positive
      Long customerId,
      @NotNull
      LocalDate transactionDate,
      @NotNull
      @Positive
      Long salesOfferId,
      @NotNull
      @Positive
      Long equipmentTypeId,
      @NotNull
      @Positive
      Integer quantity,
      @DecimalMin("0.00")
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
      @NotNull
      @Positive
      Long customerId,
      @NotNull
      LocalDate transactionDate,
      @NotNull
      @Positive
      Long equipmentId,
      @NotNull
      LocalDate rentalStartDate,
      @NotNull
      LocalDate plannedRentalEndDate,
      @DecimalMin("0.00")
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
      @NotNull
      LocalDate finishedAt,
      @DecimalMin("0.00")
      BigDecimal additionalFee
  ) {

    FinishTransactionUseCase.Command toCommand(Long transactionId) {
      return new FinishTransactionUseCase.Command(transactionId, finishedAt, additionalFee);
    }
  }
}
