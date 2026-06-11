package edu.prz.hopsops.customers.application.customer;

import edu.prz.hopsops.customers.domain.customer.Customer;
import edu.prz.hopsops.customers.domain.customer.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

  final CustomerRepository customerRepository;

  @Transactional
  public Customer execute(Command command) {
    Customer customer = customerRepository.findById(command.id())
        .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    customer.updateDetails(
        command.firstName(),
        command.lastName(),
        command.address(),
        command.phoneNumber(),
        command.email()
    );
    return customerRepository.save(customer);
  }

  public record Command(
      Long id,
      String firstName,
      String lastName,
      String address,
      String phoneNumber,
      String email
  ) {
  }
}
