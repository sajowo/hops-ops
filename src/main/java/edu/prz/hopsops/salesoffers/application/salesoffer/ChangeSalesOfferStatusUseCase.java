package edu.prz.hopsops.salesoffers.application.salesoffer;

import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOffer;
import edu.prz.hopsops.salesoffers.domain.salesoffer.SalesOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeSalesOfferStatusUseCase {

  final SalesOfferRepository salesOfferRepository;

  @Transactional
  public SalesOffer execute(Command command) {
    SalesOffer salesOffer = salesOfferRepository.findById(command.id())
        .orElseThrow(() -> new IllegalArgumentException("Sales offer not found"));
    if (command.active()) {
      salesOffer.activate();
    } else {
      salesOffer.deactivate();
    }
    return salesOfferRepository.save(salesOffer);
  }

  public record Command(Long id, boolean active) {
  }
}
