package edu.prz.hopsops.rentaloffers.application.rentaloffer;

import edu.prz.hopsops.customers.domain.customer.CustomerRepository;
import edu.prz.hopsops.foundation.domain.NotExistsException;
import edu.prz.hopsops.rentaloffers.domain.equipment.Equipment;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentRepository;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOffer;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferRepository;
import edu.prz.hopsops.rentaloffers.domain.reservation.Reservation;
import edu.prz.hopsops.rentaloffers.domain.reservation.ReservationRepository;
import edu.prz.hopsops.rentaloffers.domain.reservation.ReservationStatus;
import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReserveEquipmentUseCase {

  final RentalOfferRepository rentalOfferRepository;
  final EquipmentRepository equipmentRepository;
  final ReservationRepository reservationRepository;
  final CustomerRepository customerRepository;

  @Transactional
  public Reservation execute(Command command) {
    RentalOffer rentalOffer = rentalOfferRepository.findById(command.rentalOfferId())
        .orElseThrow(() -> new EntityNotFoundException("Rental offer not found"));
    Equipment equipment = equipmentRepository.findById(command.equipmentId())
        .orElseThrow(() -> NotExistsException.of("Equipment not found"));

    RentalOfferId rentalOfferId = new RentalOfferId(command.rentalOfferId());
    EquipmentId equipmentId = new EquipmentId(command.equipmentId());
    CustomerId customerId = new CustomerId(command.customerId());

    if (!customerRepository.existsById(customerId.id())) {
      throw NotExistsException.of("Customer not found");
    }
    if (!rentalOfferId.equals(equipment.getRentalOfferId())) {
      throw new IllegalArgumentException("Equipment does not belong to rental offer");
    }
    if (!reservationRepository.findByEquipmentIdAndStatus(equipmentId, ReservationStatus.ACTIVE)
        .isEmpty()) {
      throw new IllegalStateException("Equipment already has an active reservation");
    }

    equipment.markReserved();
    rentalOffer.markItemReserved();
    Reservation reservation = Reservation.create(
        equipmentId,
        customerId,
        command.reservedFrom(),
        command.reservedTo()
    );
    return reservationRepository.save(reservation);
  }

  public record Command(
      Long rentalOfferId,
      Long equipmentId,
      Long customerId,
      LocalDate reservedFrom,
      LocalDate reservedTo
  ) {
  }
}
