package edu.prz.hopsops.rentaloffers.application.rentaloffer;

import edu.prz.hopsops.rentaloffers.domain.equipment.Equipment;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentCondition;
import edu.prz.hopsops.rentaloffers.domain.equipment.EquipmentRepository;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOffer;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferRepository;
import edu.prz.hopsops.shared.identity.RentalOfferId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddEquipmentToRentalOfferUseCase {

  final RentalOfferRepository rentalOfferRepository;
  final EquipmentRepository equipmentRepository;

  @Transactional
  public Equipment execute(Command command) {
    RentalOffer rentalOffer = rentalOfferRepository.findById(command.rentalOfferId())
        .orElseThrow(() -> new IllegalArgumentException("Rental offer not found"));
    Equipment equipment = Equipment.create(
        new RentalOfferId(command.rentalOfferId()),
        command.serialNumber(),
        command.condition()
    );
    rentalOffer.registerEquipmentItem();
    return equipmentRepository.save(equipment);
  }

  public record Command(
      Long rentalOfferId,
      String serialNumber,
      EquipmentCondition condition
  ) {
  }
}
