package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    public static final Long ID = 2L;
    public static final String FIRSTNAME = "Jimmy";
    public static final String LASTNAME = "Doe";
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(customerRepository,CustomerMapper.INSTANCE);
    }

    @Test
    public void getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer()); customers.add(new Customer()); customers.add(new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDTO> customerDTOExtracted = customerService.getAllCustomers();

        assertEquals(3,customerDTOExtracted.size());
    }

    @Test
    public void getCustomerByName() {

        Customer jimmy = new Customer();
        jimmy.setFirstname(FIRSTNAME); jimmy.setId(ID); jimmy.setLastname(LASTNAME);

        when(customerRepository.getCustomerById(ID)).thenReturn(jimmy);

        CustomerDTO customerDTOExtracted = customerService.getCustomerById(ID);


        assertEquals(FIRSTNAME, customerDTOExtracted.getFirstname());
        assertEquals(LASTNAME, customerDTOExtracted.getLastname());
        assertEquals("/api/v1/customer/"+ID,customerDTOExtracted.getCustomerUrl());

    }
}