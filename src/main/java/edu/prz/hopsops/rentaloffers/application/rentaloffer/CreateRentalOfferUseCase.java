package edu.prz.hopsops.rentaloffers.application.rentaloffer;

import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOffer;
import edu.prz.hopsops.rentaloffers.domain.rentaloffer.RentalOfferRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRentalOfferUseCase {

  final RentalOfferRepository rentalOfferRepository;

  @Transactional
  public RentalOffer execute(Command command) {
    RentalOffer rentalOffer = RentalOffer.create(
        command.equipmentName(),
        command.category(),
        command.manufacturer(),
        command.description(),
        command.rentalPrice()
    );
    return rentalOfferRepository.save(rentalOffer);
  }

  public record Command(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal rentalPrice
  ) {
  }
}
