package edu.prz.hopsops.shared.identity;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.prz.hopsops.foundation.domain.Identity;
import jakarta.persistence.Embeddable;

@Embeddable
public record CustomerId(@JsonValue Long id) implements Identity {

  public CustomerId {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Customer id must be a positive number");
    }
  }
}
