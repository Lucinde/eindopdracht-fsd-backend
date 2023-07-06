package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.repositories.CustomerRepository;
import com.lucinde.plannerpro.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    List<Customer> customers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //Aangezien ik alleen constructors zou aanmaken in mijn models voor de test-methodes heb ik deze oplossing gebruikt
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("Albert");
        customer1.setLastName("Einstein");
        customer1.setAddress("Lindenlaan 23");
        customer1.setZip("1590AB");
        customer1.setCity("Leeuwarden");
        customer1.setPhoneNumber("06-12467890");
        customer1.setEmail("jessp@hotmail.com");
        customers.add(customer1);

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Isaac");
        customer2.setLastName("Newton");
        customer2.setAddress("Apple Street 5");
        customer2.setZip("3456YZ");
        customer2.setCity("London");
        customer2.setPhoneNumber("06-98765432");
        customer2.setEmail("newtonisaac@gmail.com");
        customers.add(customer2);

        Customer customer3 = new Customer();
        customer3.setId(3L);
        customer3.setFirstName("Marie");
        customer3.setLastName("Curie");
        customer3.setAddress("Radiation Avenue 7");
        customer3.setZip("7890XY");
        customer3.setCity("Paris");
        customer3.setPhoneNumber("06-56781234");
        customer3.setEmail("mariecurie@yahoo.com");
        customers.add(customer3);

        Customer customer4 = new Customer();
        customer4.setId(4L);
        customer4.setFirstName("Galileo");
        customer4.setLastName("Galilei");
        customer4.setAddress("Observatory Road 12");
        customer4.setZip("1234AB");
        customer4.setCity("Florence");
        customer4.setPhoneNumber("06-23456789");
        customer4.setEmail("galileogal@gmail.com");
        customers.add(customer4);

        Customer customer5 = new Customer();
        customer5.setId(5L);
        customer5.setFirstName("Nikola");
        customer5.setLastName("Tesla");
        customer5.setAddress("Wireless Street 8");
        customer5.setZip("5678CD");
        customer5.setCity("Belgrade");
        customer5.setPhoneNumber("06-87654321");
        customer5.setEmail("teslanikola@hotmail.com");
        customers.add(customer5);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        //Arrange
        when(customerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(customers);

        //Act
        List<CustomerDto> customerDtos = customerService.getAllCustomers();

        //Assert
        assertEquals(customers.size(), customerDtos.size());

    }

    @Test
    void getCustomer() {
        //Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customers.get(0)));

        //Act
        CustomerDto customerDto = customerService.getCustomer(1L);

        //Assert
        assertEquals(customers.get(0).getFirstName(), customerDto.firstName);
    }

    @Test
    void getCustomerThrowsException() {
        assertThrows(RecordNotFoundException.class, () -> customerService.getCustomer(9L));
    }

    @Test
    void getCustomerWithPagination() {
        int pageNo = 0;
        int pageSize = 3;

        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerRepository.findAll(any(PageRequest.class))).thenReturn(customerPage);

        // Act
        PageResponse<CustomerDto> pageResponse = customerService.getCustomerWithPagination(pageNo, pageSize);

        // Assert
        assertNotNull(pageResponse);
        assertEquals(customers.size(), pageResponse.count);
        assertEquals(1, pageResponse.totalPages);
        assertEquals(false, pageResponse.hasPrevious);
        assertEquals(false, pageResponse.hasNext);
        for (int i = 0; i < customers.size(); i++) {
            assertEquals(customers.get(i).getFirstName(), pageResponse.items.get(i).firstName);
        }
    }

    @Test
    void addCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void transferCustomerToDto() {
    }

    @Test
    void transferDtoToCustomer() {
    }

    @Test
    void testTransferDtoToCustomer() {
    }
}