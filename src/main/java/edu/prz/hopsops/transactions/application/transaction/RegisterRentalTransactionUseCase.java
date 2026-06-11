package edu.prz.hopsops.transactions.application.transaction;

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
public class RegisterRentalTransactionUseCase {

  final TransactionFactory transactionFactory;
  final TransactionRepository transactionRepository;
  final EquipmentRepository equipmentRepository;
  final RentalOfferRepository rentalOfferRepository;
  final ReservationRepository reservationRepository;
  final CustomerRepository customerRepository;

  @Transactional
  public Transaction execute(Command command) {
    ensureCustomerExists(command.customerId());
    Equipment equipment = equipmentRepository.findById(command.equipmentId().id())
        .orElseThrow(() -> NotExistsException.of("Equipment not found"));
    RentalOffer rentalOffer = rentalOfferRepository.findById(equipment.getRentalOfferId().id())
        .orElseThrow(() -> NotExistsException.of("Rental offer not found"));
    BigDecimal unitPrice = rentalOffer.resolveUnitPrice(command.unitPrice());
    reserveOrRentEquipment(command, equipment, rentalOffer);

    Transaction transaction = transactionFactory.create(
        new TransactionFactory.Input(command.customerId(), command.transactionDate()));
    transaction.addRentalItem(
        command.equipmentId(),
        command.rentalStartDate(),
        command.plannedRentalEndDate(),
        unitPrice
    );
    transaction.register();
    return transactionRepository.save(transaction);
  }

  private void reserveOrRentEquipment(
      Command command,
      Equipment equipment,
      RentalOffer rentalOffer
  ) {
    if (equipment.isAvailable()) {
      equipment.markRented();
      rentalOffer.markItemRented();
      return;
    }

    if (!equipment.isReserved()) {
      throw new IllegalStateException("Equipment is not available for rental");
    }

    Reservation reservation = reservationRepository
        .findFirstByEquipmentIdAndCustomerIdAndStatus(
            command.equipmentId(),
            command.customerId(),
            ReservationStatus.ACTIVE
        )
        .orElseThrow(() -> new IllegalStateException("Equipment is reserved for another customer"));
    reservation.complete();
    equipment.markRented();
    rentalOffer.markReservedItemRented();
  }

  private void ensureCustomerExists(CustomerId customerId) {
    if (!customerRepository.existsById(customerId.id())) {
      throw NotExistsException.of("Customer not found");
    }
  }

  public record Command(
      CustomerId customerId,
      LocalDate transactionDate,
      EquipmentId equipmentId,
      LocalDate rentalStartDate,
      LocalDate plannedRentalEndDate,
      BigDecimal unitPrice
  ) {
  }
}
