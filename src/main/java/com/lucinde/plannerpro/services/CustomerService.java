package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.exceptions.RelationFoundException;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.repositories.CustomerRepository;
import com.lucinde.plannerpro.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public PageResponse<CustomerDto> getCustomerWithPagination(int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<Customer> pagingCustomer = customerRepository.findAll(pageRequest);

        PageResponse<CustomerDto> response = new PageResponse<>();

        response.count = pagingCustomer.getTotalElements();
        response.totalPages = pagingCustomer.getTotalPages();
        response.hasNext = pagingCustomer.hasNext();
        response.hasPrevious = pagingCustomer.hasPrevious();
        response.items = new ArrayList<>();

        for (Customer c: pagingCustomer) {
            response.items.add(transferCustomerToDto(c));
        }

        return response;
    }

    public CustomerDto addCustomer(CustomerDto customerDto) {
        Customer customer = transferDtoToCustomer(customerDto);
        customerRepository.save(customer);

        return transferCustomerToDto(customer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer updateCustomer = transferDtoToCustomer(customerDto, id);
        updateCustomer.setId(id);
        customerRepository.save(updateCustomer);

        return transferCustomerToDto(updateCustomer);
    }

    public void deleteCustomer(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(optionalCustomer.isEmpty()) {
            throw new RecordNotFoundException("Geen klant gevonden met id: " + id);
        }
        if(!optionalCustomer.get().getTaskList().isEmpty()) {
            throw new RelationFoundException("Deze klant is gekoppeld aan een taak en mag niet verwijderd worden");
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
        return transferDtoToCustomer(customerDto, 0L);
    }

    public Customer transferDtoToCustomer(CustomerDto customerDto, Long id) {
        Customer customer;

        if(id != 0L) {
            Optional<Customer> customerOptional = customerRepository.findById(id);
            if(customerOptional.isEmpty()) {
                throw new RecordNotFoundException("Geen klant gevonden met id: " + id);
            }
            customer = customerOptional.get();
        } else {
            customer = new Customer();
        }

        // Geen setId nodig, deze genereert de database of staat in de URL
        if(customerDto.firstName != null)
            customer.setFirstName(customerDto.firstName);
        if(customerDto.lastName != null)
            customer.setLastName(customerDto.lastName);
        if(customerDto.address != null)
            customer.setAddress(customerDto.address);
        if(customerDto.zip != null)
            customer.setZip(customerDto.zip);
        if(customerDto.city != null)
            customer.setCity(customerDto.city);
        if(customerDto.phoneNumber != null)
            customer.setPhoneNumber(customerDto.phoneNumber);
        if(customerDto.email != null)
            customer.setEmail(customerDto.email);
        if(customerDto.taskList != null)
            customer.setTaskList(customerDto.taskList);

        return customer;
    }
}
