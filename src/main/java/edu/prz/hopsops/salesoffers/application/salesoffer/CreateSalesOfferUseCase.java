package edu.prz.hopsops.salesoffers.application.salesoffer;

import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOffer;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOfferRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSalesOfferUseCase {

  final SalesOfferRepository salesOfferRepository;

  @Transactional
  public SalesOffer execute(Command command) {
    SalesOffer salesOffer = SalesOffer.create(
        command.equipmentName(),
        command.category(),
        command.manufacturer(),
        command.description(),
        command.salePrice()
    );
    return salesOfferRepository.save(salesOffer);
  }

  public record Command(
      String equipmentName,
      String category,
      String manufacturer,
      String description,
      BigDecimal salePrice
  ) {
  }
}
