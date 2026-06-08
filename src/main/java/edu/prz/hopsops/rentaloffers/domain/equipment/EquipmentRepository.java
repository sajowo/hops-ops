package edu.prz.hopsops.rentaloffers.domain.equipment;

import edu.prz.hopsops.shared.identity.RentalOfferId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

  List<Equipment> findByRentalOfferId(RentalOfferId rentalOfferId);
}
