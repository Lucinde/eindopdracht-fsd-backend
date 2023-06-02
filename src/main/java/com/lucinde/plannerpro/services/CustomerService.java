package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.repositories.CustomerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDto> getAllCustomers() {
        Iterable<Customer> customers = customerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<CustomerDto> customerDtos = new ArrayList<>();

        for (Customer c: customers) {
            customerDtos.add(transferCustomerToDto(c));
        }

        return customerDtos;
    }

    public CustomerDto getCustomer(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if(customerOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen klant gevonden met id: " + id);
        }

        Customer customer = customerOptional.get();

        return transferCustomerToDto(customer);
    }

    public CustomerDto addCustomer(CustomerDto customerDto) {
        Customer customer = transferDtoToCustomer(customerDto);
        customerRepository.save(customer);

        return transferCustomerToDto(customer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen klant gevonden met id: " + id);
        }

        Customer updateCustomer = transferDtoToCustomer(customerDto);
        updateCustomer.setId(id);
        customerRepository.save(updateCustomer);

        return transferCustomerToDto(updateCustomer);
    }

    public void deleteCustomer(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(optionalCustomer.isEmpty()) {
            throw new RecordNotFoundException("Geen klant gevonden met id: " + id);
        }
        customerRepository.deleteById(id);
    }



    public CustomerDto transferCustomerToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();

        customerDto.id = customer.getId();
        customerDto.firstName = customer.getFirstName();
        customerDto.lastName = customer.getLastName();
        customerDto.address = customer.getAddress();
        customerDto.zip = customer.getZip();
        customerDto.city = customer.getCity();
        customerDto.phoneNumber = customer.getPhoneNumber();
        customerDto.email = customer.getEmail();
        customerDto.taskList = customer.getTaskList();

        return customerDto;
    }

    public Customer transferDtoToCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();

        // Geen setId nodig, deze genereert de database of staat in de URL
        customer.setFirstName(customerDto.firstName);
        customer.setLastName(customerDto.lastName);
        customer.setAddress(customerDto.address);
        customer.setZip(customerDto.zip);
        customer.setCity(customerDto.city);
        customer.setPhoneNumber(customerDto.phoneNumber);
        customer.setEmail(customerDto.email);
        customer.setTaskList(customerDto.taskList);

        return customer;
    }
}
