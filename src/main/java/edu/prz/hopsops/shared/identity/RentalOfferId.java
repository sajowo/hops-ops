package edu.prz.hopsops.shared.identity;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.prz.hopsops.foundation.domain.Identity;
import jakarta.persistence.Embeddable;

@Embeddable
public record RentalOfferId(@JsonValue Long id) implements Identity {

  public RentalOfferId {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Rental offer id must be a positive number");
    }
  }
}
