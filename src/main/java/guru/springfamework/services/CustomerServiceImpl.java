package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private final String URL = "/api/v1/customer/";

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        Iterable<Customer> customerIterableList = customerRepository.findAll();

        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customerIterableList) {
            CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
            customerDTO.setCustomerUrl(URL+customer.getId());
            customerDTOS.add(customerDTO);
        }

        return customerDTOS;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        CustomerDTO customerDTO =customerMapper.customerToCustomerDTO(
                customerRepository.getCustomerById(id));
        customerDTO.setCustomerUrl(URL+id);
        return customerDTO;
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        return saveAndReturnDTO(customerMapper.customerDtoToCustomer(customerDTO));
    }

    @Override
    public CustomerDTO saveCustomerByDTO(long id, CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDTO);
        customer.setId(id);
        return saveAndReturnDTO(customer);
    }

    private CustomerDTO saveAndReturnDTO(Customer customer) {
        //Saving the customer in repository:
        Customer savedCustomer = customerRepository.save(customer);
        //Creating a DTO to return
        CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(savedCustomer);

        returnDTO.setCustomerUrl(URL+savedCustomer.getId());

        return returnDTO;
    }
    //Path operation means that fields that are not given, are not updated
    //(Different from Update operation!)
    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {

            if(customerDTO.getFirstname() != null){
                customer.setFirstname(customerDTO.getFirstname());
            }

            if(customerDTO.getLastname() != null){
                customer.setLastname(customerDTO.getLastname());
            }
            CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(
                    customerRepository.save(customer));
            returnDTO.setCustomerUrl(URL+id);

            return returnDTO;
        }).orElseThrow(RuntimeException::new); //todo implement better exception handling;

    }
}
