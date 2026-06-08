package edu.prz.hopsops.customers.application.customer;

import edu.prz.hopsops.customers.domain.customer.Customer;
import edu.prz.hopsops.customers.domain.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {

  final CustomerRepository customerRepository;

  @Transactional
  public Customer execute(Command command) {
    Customer customer = Customer.create(
        command.firstName(),
        command.lastName(),
        command.address(),
        command.phoneNumber(),
        command.email()
    );
    return customerRepository.save(customer);
  }

  public record Command(
      String firstName,
      String lastName,
      String address,
      String phoneNumber,
      String email
  ) {
  }
}
