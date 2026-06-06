package edu.prz.hopsops.accounts.domain.useraccount;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccount extends BaseEntity {

  String username;

  String passwordHash;

  String firstName;

  String lastName;

  String position;

  @Enumerated(EnumType.STRING)
  UserAccountRole role;

  Boolean active;
}
