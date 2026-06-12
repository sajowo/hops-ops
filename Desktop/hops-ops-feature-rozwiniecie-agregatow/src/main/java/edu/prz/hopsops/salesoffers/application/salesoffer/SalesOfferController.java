package edu.prz.hopsops.salesoffers.application.salesoffer;

import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOffer;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOfferRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales-offers")
@RequiredArgsConstructor
public class SalesOfferController {

  final SalesOfferRepository salesOfferRepository;
  final CreateSalesOfferUseCase createSalesOfferUseCase;
  final UpdateSalesOfferUseCase updateSalesOfferUseCase;
  final ChangeSalesOfferStatusUseCase changeSalesOfferStatusUseCase;

  @PostMapping
  public ResponseEntity<SalesOffer> create(@Valid @RequestBody SalesOfferRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createSalesOfferUseCase.execute(request.toCreateCommand()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SalesOffer> update(
      @PathVariable Long id,
      @Valid @RequestBody SalesOfferRequest request
  ) {
    return ResponseEntity.ok(updateSalesOfferUseCase.execute(request.toUpdateCommand(id)));
  }

  @PostMapping("/{id}/activate")
  public ResponseEntity<SalesOffer> activate(@PathVariable Long id) {
    return ResponseEntity.ok(changeSalesOfferStatusUseCase.execute(
        new ChangeSalesOfferStatusUseCase.Command(id, true)));
  }

  @PostMapping("/{id}/deactivate")
  public ResponseEntity<SalesOffer> deactivate(@PathVariable Long id) {
    return ResponseEntity.ok(changeSalesOfferStatusUseCase.execute(
        new ChangeSalesOfferStatusUseCase.Command(id, false)));
  }

  @GetMapping
  public ResponseEntity<List<SalesOffer>> getAll() {
    return ResponseEntity.ok(salesOfferRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SalesOffer> getOne(@PathVariable Long id) {
    return salesOfferRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  public record SalesOfferRequest(
      @NotBlank(message = "Equipment name is required")
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      @NotNull(message = "Sale price is required")
      @DecimalMin(value = "0.01", inclusive = true, message = "Sale price must be positive")
      @Digits(integer = 18, fraction = 2, message = "Sale price must be a valid number")
      BigDecimal salePrice
  ) {

    CreateSalesOfferUseCase.Command toCreateCommand() {
      return new CreateSalesOfferUseCase.Command(
          equipmentName,
          category,
          manufacturer,
          description,
          salePrice
      );
    }

    UpdateSalesOfferUseCase.Command toUpdateCommand(Long id) {
      return new UpdateSalesOfferUseCase.Command(
          id,
          equipmentName,
          category,
          manufacturer,
          description,
          salePrice
      );
    }
  }
}
