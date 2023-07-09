package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.repositories.CustomerRepository;
import com.lucinde.plannerpro.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
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
        customerDto1.firstName = "Albert";
        customerDto1.lastName = "Einstein";
        customerDto1.address = "Lindenlaan 23";
        customerDto1.zip = "1590AB";
        customerDto1.city = "Leeuwarden";
        customerDto1.phoneNumber = "06-12467890";
        customerDto1.email = "jessp@hotmail.com";
        customerDto1.taskList = new ArrayList<>();

        customerDto2.id = 5L;
        customerDto2.firstName = "Isaac";
        customerDto2.lastName = "Newton";
        customerDto2.address = "Apple Street 5";
        customerDto2.zip = "3456YZ";
        customerDto2.city = "London";
        customerDto2.phoneNumber = "06-98765432";
        customerDto2.email = "newtonisaac@gmail.com";
        customerDto2.taskList = new ArrayList<>();

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
    }

    @Test
    @WithMockUser(username="John", roles="ADMIN")
    void getAllCustomers() throws Exception {

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                // $[?(@.id == 1)] checkt voor de gegevens van de persoon in de lijst met dit ID, zo maakt de volgorde van de lijst niet uit
                .andExpect(jsonPath("$[?(@.id == 1)].firstName").value("Albert"))
                .andExpect(jsonPath("$[?(@.id == 1)].lastName").value("Einstein"))
                .andExpect(jsonPath("$[?(@.id == 1)].address").value("Lindenlaan 23"))
                .andExpect(jsonPath("$[?(@.id == 1)].zip").value("1590AB"))
                .andExpect(jsonPath("$[?(@.id == 1)].city").value("Leeuwarden"))
                .andExpect(jsonPath("$[?(@.id == 1)].phoneNumber").value("06-12467890"))
                .andExpect(jsonPath("$[?(@.id == 1)].email").value("jessp@hotmail.com"))
                .andExpect(jsonPath("$[?(@.id == 1)].taskList", hasSize(1)))
                .andExpect(jsonPath("$[?(@.id == 3)].firstName").value("Marie"))
                .andExpect(jsonPath("$[?(@.id == 3)].lastName").value("Curie"))
                .andExpect(jsonPath("$[?(@.id == 3)].address").value("Radiation Avenue 7"))
                .andExpect(jsonPath("$[?(@.id == 3)].zip").value("7890XY"))
                .andExpect(jsonPath("$[?(@.id == 3)].city").value("Paris"))
                .andExpect(jsonPath("$[?(@.id == 3)].phoneNumber").value("06-56781234"))
                .andExpect(jsonPath("$[?(@.id == 3)].email").value("mariecurie@yahoo.com"))
                .andExpect(jsonPath("$[?(@.id == 3)].taskList", hasSize(1)));

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