package edu.prz.hopsops.salesoffers.application.salesoffer;

import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOffer;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOfferRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateSalesOfferUseCase {

  final SalesOfferRepository salesOfferRepository;

  @Transactional
  public SalesOffer execute(Command command) {
    SalesOffer salesOffer = salesOfferRepository.findById(command.id())
        .orElseThrow(() -> new IllegalArgumentException("Sales offer not found"));
    salesOffer.updateDetails(
        command.equipmentName(),
        command.category(),
        command.manufacturer(),
        command.description(),
        command.salePrice()
    );
    return salesOfferRepository.save(salesOffer);
  }

  public record Command(
      Long id,
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal salePrice
  ) {
  }
}
