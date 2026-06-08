package edu.prz.hopsops.shared.identity;

import com.fasterxml.jackson.annotation.JsonValue;
import edu.prz.hopsops.foundation.domain.Identity;
import jakarta.persistence.Embeddable;

@Embeddable
public record EquipmentTypeId(@JsonValue Long id) implements Identity {

  public EquipmentTypeId {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Equipment type id must be a positive number");
    }
  }
}
