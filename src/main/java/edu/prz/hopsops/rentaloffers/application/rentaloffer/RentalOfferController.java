package edu.prz.hopsops.rentaloffers.application.rentaloffer;

import edu.prz.hopsops.rentaloffers.domain.equipment.Equipment;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentCondition;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentRepository;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOffer;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferRepository;
import edu.prz.hopsops.rentaloffers.domain.reservation.Reservation;
import edu.prz.hopsops.rentaloffers.domain.reservation.ReservationRepository;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@RequestMapping("/api/rental-offers")
@RequiredArgsConstructor
public class RentalOfferController {

  final RentalOfferRepository rentalOfferRepository;
  final EquipmentRepository equipmentRepository;
  final ReservationRepository reservationRepository;
  final CreateRentalOfferUseCase createRentalOfferUseCase;
  final UpdateRentalOfferUseCase updateRentalOfferUseCase;
  final AddEquipmentToRentalOfferUseCase addEquipmentToRentalOfferUseCase;
  final ReserveEquipmentUseCase reserveEquipmentUseCase;

  @PostMapping
  public ResponseEntity<RentalOffer> create(@RequestBody RentalOfferRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createRentalOfferUseCase.execute(request.toCreateCommand()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<RentalOffer> update(
      @PathVariable Long id,
      @RequestBody RentalOfferRequest request
  ) {
    return ResponseEntity.ok(updateRentalOfferUseCase.execute(request.toUpdateCommand(id)));
  }

  @PostMapping("/{id}/equipment")
  public ResponseEntity<Equipment> addEquipment(
      @PathVariable Long id,
      @RequestBody AddEquipmentRequest request
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(addEquipmentToRentalOfferUseCase.execute(request.toCommand(id)));
  }

  @PostMapping("/{id}/reservations")
  public ResponseEntity<Reservation> reserve(
      @PathVariable Long id,
      @RequestBody ReserveEquipmentRequest request
  ) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(reserveEquipmentUseCase.execute(request.toCommand(id)));
  }

  @GetMapping
  public ResponseEntity<List<RentalOffer>> getAll() {
    return ResponseEntity.ok(rentalOfferRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<RentalOffer> getOne(@PathVariable Long id) {
    return rentalOfferRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}/equipment")
  public ResponseEntity<List<Equipment>> getEquipment(@PathVariable Long id) {
    return ResponseEntity.ok(equipmentRepository.findByRentalOfferId(new RentalOfferId(id)));
  }

  @GetMapping("/reservations")
  public ResponseEntity<List<Reservation>> getReservations() {
    return ResponseEntity.ok(reservationRepository.findAll());
  }

  public record RentalOfferRequest(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
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
      String serialNumber,
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
      Long equipmentId,
      Long customerId,
      LocalDate reservedFrom,
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
