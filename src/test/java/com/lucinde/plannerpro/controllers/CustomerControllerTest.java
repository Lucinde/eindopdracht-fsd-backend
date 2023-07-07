package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.filters.JwtRequestFilter;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.repositories.CustomerRepository;
import com.lucinde.plannerpro.services.CustomerService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    Customer customer1 = new Customer();
    Customer customer2 = new Customer();
    Customer customer3 = new Customer();
    CustomerDto customerDto1 = new CustomerDto();
    CustomerDto customerDto2 = new CustomerDto();
    List<Customer> customers = new ArrayList<>();

    @BeforeEach
    void setUp() {
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

        customerDto1.id = 4L;
        customerDto1.firstName = "Galileo";
        customerDto1.lastName = "Galilei";
        customerDto1.address = "Observatory Road 12";
        customerDto1.zip = "1234AB";
        customerDto1.city = "Florence";
        customerDto1.phoneNumber = "06-23456789";
        customerDto1.email = "galileogal@gmail.com";
        customerDto1.taskList = new ArrayList<>();

        customerDto2.id = 5L;
        customerDto2.firstName = "Nikola";
        customerDto2.lastName = "Tesla";
        customerDto2.address = "Wireless Street 8";
        customerDto2.zip = "5678CD";
        customerDto2.city = "Belgrade";
        customerDto2.phoneNumber = "06-87654321";
        customerDto2.email = "teslanikola@hotmail.com";
        customerDto2.taskList = (new ArrayList<>());

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
    }

    @Test
//    @WithMockUser(username="John", roles="ROLE_ADMIN")
    void getAllCustomers() throws Exception {

    }

    @Test
    void getCustomer() throws Exception{
        mockMvc.perform(get("/customers/{id}", customer1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("firstName").value("Albert"))
                .andExpect(jsonPath("lastName").value("Einstein"))
                .andExpect(jsonPath("address").value("Lindenlaan 23"))
                .andExpect(jsonPath("zip").value("1590AB"))
                .andExpect(jsonPath("city").value("Leeuwarden"))
                .andExpect(jsonPath("phoneNumber").value("06-12467890"))
                .andExpect(jsonPath("email").value("jessp@hotmail.com"))
                .andExpect(jsonPath("taskList").isEmpty());
    }

    @Test
    void getTasksWithPagination() {
    }

    @Test
    void addCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/{id}", customer2.getId()))
                .andExpect(status().isNoContent());
    }
}