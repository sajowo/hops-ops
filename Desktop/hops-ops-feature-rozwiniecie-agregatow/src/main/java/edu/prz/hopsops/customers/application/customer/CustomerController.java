package edu.prz.hopsops.customers.application.customer;

import edu.prz.hopsops.customers.domain.customer.Customer;
import edu.prz.hopsops.customers.domain.customer.CustomerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

  final CustomerRepository customerRepository;
  final CreateCustomerUseCase createCustomerUseCase;
  final UpdateCustomerUseCase updateCustomerUseCase;

  @PostMapping
  public ResponseEntity<Customer> create(@Valid @RequestBody CustomerRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createCustomerUseCase.execute(request.toCreateCommand()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Customer> update(
      @PathVariable Long id,
      @Valid @RequestBody CustomerRequest request
  ) {
    return ResponseEntity.ok(updateCustomerUseCase.execute(request.toUpdateCommand(id)));
  }

  @GetMapping
  public ResponseEntity<List<Customer>> getAll() {
    return ResponseEntity.ok(customerRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getOne(@PathVariable Long id) {
    return customerRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  public record CustomerRequest(
      @NotBlank(message = "First name is required")
      @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźżĄĆĘŁŃÓŚŹŻ]*$", message = "First name must contain only letters and start with an uppercase letter")
      String firstName,
      @NotBlank(message = "Last name is required")
      @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźżĄĆĘŁŃÓŚŹŻ]*$", message = "Last name must contain only letters and start with an uppercase letter")
      String lastName,
      String address,
      @NotBlank(message = "Phone number is required")
      @Pattern(regexp = "^\\d{9}$", message = "Phone number must contain exactly 9 digits")
      String phoneNumber,
      @NotBlank(message = "Email is required")
      @Email(message = "Email must be valid")
      String email
  ) {

    CreateCustomerUseCase.Command toCreateCommand() {
      return new CreateCustomerUseCase.Command(
          firstName,
          lastName,
          address,
          phoneNumber,
          email
      );
    }

    UpdateCustomerUseCase.Command toUpdateCommand(Long id) {
      return new UpdateCustomerUseCase.Command(
          id,
          firstName,
          lastName,
          address,
          phoneNumber,
          email
      );
    }
  }
}
