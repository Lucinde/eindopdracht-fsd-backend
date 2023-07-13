package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAllByFirstNameOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
}
