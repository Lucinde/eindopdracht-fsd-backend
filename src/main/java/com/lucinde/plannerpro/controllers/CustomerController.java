package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.helpers.Helpers;
import com.lucinde.plannerpro.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final Helpers helpers = new Helpers();

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.getCustomer(id));
    }

    @PostMapping
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody CustomerDto customerDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(helpers.fieldErrorBuilder(br));
        }
        CustomerDto addedCustomer = customerService.addCustomer(customerDto);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedCustomer.id)));
        return ResponseEntity.created(uri).body(addedCustomer);
    }

    //todo: Add postmapping to add a task to a customer

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(helpers.fieldErrorBuilder(br));
        }
        CustomerDto updateCustomer = customerService.updateCustomer(id, customerDto);
        return ResponseEntity.ok().body(updateCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
