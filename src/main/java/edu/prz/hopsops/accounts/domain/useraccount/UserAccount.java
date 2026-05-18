package edu.prz.hopsops.accounts.domain.useraccount;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserAccount {

  @Id
  Long id;

  String username;

  String passwordHash;

  String firstName;

  String lastName;

  String position;

  @Enumerated(EnumType.STRING)
  UserAccountRole role;

  Boolean active;
}
