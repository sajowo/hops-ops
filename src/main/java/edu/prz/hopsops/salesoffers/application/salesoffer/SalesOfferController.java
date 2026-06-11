package edu.prz.hopsops.salesoffers.application.salesoffer;

import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOffer;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOfferRepository;
import edu.prz.hopsops.foundation.application.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "sales / sales-offers", description = "Sales offers")
@RequestMapping("/api/sales-offers")
@RequiredArgsConstructor
public class SalesOfferController extends BaseController {

  final SalesOfferRepository salesOfferRepository;
  final CreateSalesOfferUseCase createSalesOfferUseCase;
  final UpdateSalesOfferUseCase updateSalesOfferUseCase;
  final ChangeSalesOfferStatusUseCase changeSalesOfferStatusUseCase;

  @PostMapping
  public ResponseEntity<SalesOffer> create(@RequestBody @Valid SalesOfferRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createSalesOfferUseCase.execute(request.toCreateCommand()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SalesOffer> update(
      @PathVariable Long id,
      @RequestBody @Valid SalesOfferRequest request
  ) {
    return ResponseEntity.ok(updateSalesOfferUseCase.execute(request.toUpdateCommand(id)));
  }

  @PostMapping(_ID + "/activate")
  public ResponseEntity<SalesOffer> activate(@PathVariable Long id) {
    return ResponseEntity.ok(changeSalesOfferStatusUseCase.execute(
        new ChangeSalesOfferStatusUseCase.Command(id, true)));
  }

  @PostMapping(_ID + "/deactivate")
  public ResponseEntity<SalesOffer> deactivate(@PathVariable Long id) {
    return ResponseEntity.ok(changeSalesOfferStatusUseCase.execute(
        new ChangeSalesOfferStatusUseCase.Command(id, false)));
  }

  @GetMapping
  public ResponseEntity<Page<SalesOffer>> getAll(Pageable pageable) {
    return ResponseEntity.ok(salesOfferRepository.findAll(pageable));
  }

  @GetMapping(_ID)
  public ResponseEntity<SalesOffer> getOne(@PathVariable Long id) {
    return salesOfferRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  public record SalesOfferRequest(
      @NotBlank String equipmentName,
      String category,
      String manufacturer,
      String description,
      @NotNull
      @DecimalMin("0.00")
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
