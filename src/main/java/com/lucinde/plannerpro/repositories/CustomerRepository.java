package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
