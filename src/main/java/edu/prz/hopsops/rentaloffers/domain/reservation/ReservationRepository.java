package edu.prz.hopsops.rentaloffers.domain.reservation;

import edu.prz.hopsops.shared.identity.CustomerId;
import edu.prz.hopsops.shared.identity.EquipmentId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findByEquipmentIdAndStatus(EquipmentId equipmentId, ReservationStatus status);

  Optional<Reservation> findFirstByEquipmentIdAndCustomerIdAndStatus(
      EquipmentId equipmentId,
      CustomerId customerId,
      ReservationStatus status
  );
}
