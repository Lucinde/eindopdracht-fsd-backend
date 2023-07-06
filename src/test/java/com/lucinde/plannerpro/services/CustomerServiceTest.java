package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.exceptions.RelationFoundException;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.models.Task;
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
import static org.mockito.Mockito.*;

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
        customer1.setTaskList(new ArrayList<>());
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
        customer2.setTaskList(new ArrayList<>());
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
        customer3.setTaskList(new ArrayList<>());
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
        customer4.setTaskList(new ArrayList<>());
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
        customer5.setTaskList(new ArrayList<>());
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
        assertFalse(pageResponse.hasPrevious);
        assertFalse(pageResponse.hasNext);
        for (int i = 0; i < customers.size(); i++) {
            assertEquals(customers.get(i).getFirstName(), pageResponse.items.get(i).firstName);
        }
    }

    @Test
    void addCustomer() {
        // Arrange
        CustomerDto customerDto6 = new CustomerDto();
        customerDto6.id = 6L;
        customerDto6.firstName = "Ada";
        customerDto6.lastName = "Lovelace";
        customerDto6.address = "Analytical Street 10";
        customerDto6.zip = "4567YZ";
        customerDto6.city = "London";
        customerDto6.phoneNumber = "06-12345678";
        customerDto6.email = "ada.lovelace@example.com";

        Customer customer = new Customer();
        customer.setId(customerDto6.id);
        customer.setFirstName(customerDto6.firstName);
        customer.setLastName(customerDto6.lastName);
        customer.setAddress(customerDto6.address);
        customer.setZip(customerDto6.zip);
        customer.setCity(customerDto6.city);
        customer.setPhoneNumber(customerDto6.phoneNumber);
        customer.setEmail(customerDto6.email);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        customerService.addCustomer(customerDto6);
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());
        Customer savedCustomer = customerArgumentCaptor.getValue();

        // Assert
        assertEquals(customerDto6.firstName, savedCustomer.getFirstName());
        assertEquals(customerDto6.lastName, savedCustomer.getLastName());
        assertEquals(customerDto6.address, savedCustomer.getAddress());
        assertEquals(customerDto6.zip, savedCustomer.getZip());
        assertEquals(customerDto6.city, savedCustomer.getCity());
        assertEquals(customerDto6.phoneNumber, savedCustomer.getPhoneNumber());
        assertEquals(customerDto6.email, savedCustomer.getEmail());
    }

    @Test
    void updateCustomer() {
        // Arrange
        Long customerId = 1L;
        CustomerDto customerDto6 = new CustomerDto();
        customerDto6.id = 6L;
        customerDto6.firstName = "Ada";
        customerDto6.lastName = "Lovelace";
        customerDto6.address = "Analytical Street 10";
        customerDto6.zip = "4567YZ";
        customerDto6.city = "London";
        customerDto6.phoneNumber = "06-12345678";
        customerDto6.email = "ada.lovelace@example.com";

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setFirstName("Oude");
        existingCustomer.setLastName("Naam");
        existingCustomer.setAddress("Hoofdstraat 1");
        existingCustomer.setZip("1234AB");
        existingCustomer.setCity("Eerste Woonplaats");
        existingCustomer.setPhoneNumber("06-12345678");
        existingCustomer.setEmail("eerstemail@example.com");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        // Act
        CustomerDto updatedCustomer = customerService.updateCustomer(customerId, customerDto6);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(any(Customer.class));

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(customerDto6.firstName, updatedCustomer.firstName);
        assertEquals(customerDto6.lastName, updatedCustomer.lastName);
        assertEquals(customerDto6.address, updatedCustomer.address);
        assertEquals(customerDto6.zip, updatedCustomer.zip);
        assertEquals(customerDto6.city, updatedCustomer.city);
        assertEquals(customerDto6.phoneNumber, updatedCustomer.phoneNumber);
        assertEquals(customerDto6.email, updatedCustomer.email);
    }

    @Test
    void deleteCustomer() {
        //Arrange
        when(customerRepository.findById(2l)).thenReturn(Optional.of(customers.get(1)));

        //Act
        customerService.deleteCustomer(2L);

        //Assert
        verify(customerRepository).deleteById(2L);

    }

    @Test
    void deleteCustomerThrowsExceptionRecord() {
        //Arrange
        when(customerRepository.findById(9L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RecordNotFoundException.class, () -> {
            customerService.deleteCustomer(9L);
        });
    }

    @Test
    void deleteCustomerThrowsExceptionRelation() {
        //Arrange
        Customer customer = customers.get(0);
        ArrayList<Task> mockTaskList = mock(ArrayList.class);
        customer.setTaskList(mockTaskList);

        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        mockTaskList.addAll(List.of(task1, task2, task3));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act and Assert
        assertThrows(RelationFoundException.class, () -> {
            customerService.deleteCustomer(1L);
        });
    }

    @Test
    void transferCustomerToDto() {
    }

    @Test
    void transferDtoToCustomerThrowsException() {
        CustomerDto customerDto6 = new CustomerDto();
        customerDto6.id = 6L;
        customerDto6.firstName = "Ada";
        customerDto6.lastName = "Lovelace";
        customerDto6.address = "Analytical Street 10";
        customerDto6.zip = "4567YZ";
        customerDto6.city = "London";
        customerDto6.phoneNumber = "06-12345678";
        customerDto6.email = "ada.lovelace@example.com";

        when(customerRepository.findById(9L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RecordNotFoundException.class, () -> {
            customerService.transferDtoToCustomer(customerDto6, 9L);
        });
    }

    @Test
    void testTransferDtoToCustomer() {
    }

    @Test
    void transferDtoToCustomerSetTaskList() {
        // Arrange
        ArrayList<Task> mockTaskList = mock(ArrayList.class);
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        mockTaskList.addAll(List.of(task1, task2, task3));

        CustomerDto customerDto6 = new CustomerDto();
        customerDto6.id = 6L;
        customerDto6.firstName = "Ada";
        customerDto6.lastName = "Lovelace";
        customerDto6.address = "Analytical Street 10";
        customerDto6.zip = "4567YZ";
        customerDto6.city = "London";
        customerDto6.phoneNumber = "06-12345678";
        customerDto6.email = "ada.lovelace@example.com";
        customerDto6.taskList = mockTaskList;

        // Act
        Customer result = customerService.transferDtoToCustomer(customerDto6);

        // Assert
        assertNotNull(result);
        assertEquals(customerDto6.taskList, result.getTaskList());
    }
}