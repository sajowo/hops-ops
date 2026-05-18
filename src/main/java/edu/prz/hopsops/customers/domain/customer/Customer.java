package edu.prz.hopsops.customers.domain.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customer {

  @Id
  Long id;

  String firstName;

  String lastName;

  String address;

  String phoneNumber;

  String email;
}
