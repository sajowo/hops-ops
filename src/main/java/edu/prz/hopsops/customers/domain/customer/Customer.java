package edu.prz.hopsops.customers.domain.customer;

import edu.prz.hopsops.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity {

  String firstName;

  String lastName;

  String address;

  String phoneNumber;

  String email;

  public static Customer create(
      String firstName,
      String lastName,
      String address,
      String phoneNumber,
      String email
  ) {
    Customer customer = new Customer();
    customer.updateDetails(firstName, lastName, address, phoneNumber, email);
    return customer;
  }

  public void updateDetails(
      String firstName,
      String lastName,
      String address,
      String phoneNumber,
      String email
  ) {
    if (firstName == null || firstName.isBlank()) {
      throw new IllegalArgumentException("First name is required");
    }
    if (lastName == null || lastName.isBlank()) {
      throw new IllegalArgumentException("Last name is required");
    }

    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }
}
