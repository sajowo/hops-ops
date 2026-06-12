package edu.prz.hopsops.rentaloffers.application.rentaloffer;

import edu.prz.hopsops.rentaloffers.domain.equipment.Equipment;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentCondition;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentRepository;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOffer;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferRepository;
import edu.prz.hopsops.rentaloffers.domain.reservation.Reservation;
import edu.prz.hopsops.rentaloffers.domain.reservation.ReservationRepository;
import edu.prz.hopsops.foundation.application.BaseController;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Tag(name = "rental / rental-offers", description = "Rental offers")
@RequestMapping("/api/rental-offers")
@RequiredArgsConstructor
public class RentalOfferController extends BaseController {

  final RentalOfferRepository rentalOfferRepository;
  final EquipmentRepository equipmentRepository;
  final ReservationRepository reservationRepository;
  final CreateRentalOfferUseCase createRentalOfferUseCase;
  final UpdateRentalOfferUseCase updateRentalOfferUseCase;
  final AddEquipmentToRentalOfferUseCase addEquipmentToRentalOfferUseCase;
  final ReserveEquipmentUseCase reserveEquipmentUseCase;

  @PostMapping
  public ResponseEntity<RentalOffer> create(@RequestBody @Valid RentalOfferRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createRentalOfferUseCase.execute(request.toCreateCommand()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<RentalOffer> update(
      @PathVariable Long id,
      @RequestBody @Valid RentalOfferRequest request
  ) {
    return ResponseEntity.ok(updateRentalOfferUseCase.execute(request.toUpdateCommand(id)));
  }

  @PostMapping(_ID + "/equipment")
  public ResponseEntity<Equipment> addEquipment(
      @PathVariable Long id,
      @RequestBody @Valid AddEquipmentRequest request
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(addEquipmentToRentalOfferUseCase.execute(request.toCommand(id)));
  }

  @PostMapping(_ID + "/reservations")
  public ResponseEntity<Reservation> reserve(
      @PathVariable Long id,
      @RequestBody @Valid ReserveEquipmentRequest request
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(reserveEquipmentUseCase.execute(request.toCommand(id)));
  }

  @GetMapping
  public ResponseEntity<Page<RentalOffer>> getAll(Pageable pageable) {
    return ResponseEntity.ok(rentalOfferRepository.findAll(pageable));
  }

  @GetMapping(_ID)
  public ResponseEntity<RentalOffer> getOne(@PathVariable Long id) {
    return rentalOfferRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping(_ID + "/equipment")
  public ResponseEntity<Page<Equipment>> getEquipment(@PathVariable Long id, Pageable pageable) {
    return ResponseEntity.ok(equipmentRepository.findByRentalOfferId(new RentalOfferId(id), pageable));
  }

  @GetMapping("/reservations")
  public ResponseEntity<Page<Reservation>> getReservations(Pageable pageable) {
    return ResponseEntity.ok(reservationRepository.findAll(pageable));
  }

  public record RentalOfferRequest(
      @NotBlank String equipmentName,
      String category,
      String manufacturer,
      String description,
      @NotNull
      @DecimalMin("0.00")
      BigDecimal rentalPrice
  ) {

    CreateRentalOfferUseCase.Command toCreateCommand() {
      return new CreateRentalOfferUseCase.Command(
          equipmentName,
          category,
          manufacturer,
          description,
          rentalPrice
      );
    }

    UpdateRentalOfferUseCase.Command toUpdateCommand(Long id) {
      return new UpdateRentalOfferUseCase.Command(
          id,
          equipmentName,
          category,
          manufacturer,
          description,
          rentalPrice
      );
    }
  }

  public record AddEquipmentRequest(
      @NotBlank String serialNumber,
      EquipmentCondition condition
  ) {

    AddEquipmentToRentalOfferUseCase.Command toCommand(Long rentalOfferId) {
      return new AddEquipmentToRentalOfferUseCase.Command(
          rentalOfferId,
          serialNumber,
          condition
      );
    }
  }

  public record ReserveEquipmentRequest(
      @NotNull
      @Positive
      Long equipmentId,
      @NotNull
      @Positive
      Long customerId,
      @NotNull
      LocalDate reservedFrom,
      @NotNull
      LocalDate reservedTo
  ) {

    ReserveEquipmentUseCase.Command toCommand(Long rentalOfferId) {
      return new ReserveEquipmentUseCase.Command(
          rentalOfferId,
          equipmentId,
          customerId,
          reservedFrom,
          reservedTo
      );
    }
  }
}
