package guru.springframework.controllers.v1;

import guru.springframework.model.CustomerDTO;
import guru.springframework.model.CustomerListDTO;
import guru.springframework.services.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(description = "This is my Customer Controller")
@RestController
@RequestMapping(CustomerController.BASE_URL)
public class CustomerController {

    public static final String BASE_URL = "/api/v1/customers";
    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "This will get a list of customers.",
            notes = "Consuming this Api will return a consumerListDTO with all Customers.")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CustomerListDTO getAllCustomers() {
        CustomerListDTO customerListDTO = new CustomerListDTO();
        customerListDTO.getCustomers().addAll(customerService.getAllCustomers());
        return customerListDTO;

    }
    @ApiOperation(value = "This will get a singular customer.",
            notes = "Consuming this Api will return a customer filtered by ID.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @ApiOperation(value = "This will create a customer.",
            notes = "Consuming this Api will create a customer from a resource (customerDTO).")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createNewCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.createNewCustomer(customerDTO);
    }

    @ApiOperation(value = "This will update a customer.",
            notes = "Consuming this Api will update a customer from a resource (customerDTO).")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO updateCustomer(@PathVariable Long id,
                                      @RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomerByDTO(id, customerDTO);
    }

    @ApiOperation(value = "This will patch a customer.",
            notes = "Consuming this Api will patch a customer from a resource (customerDTO).")
    @PatchMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO patchCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return customerService.patchCustomer(id, customerDTO);
    }

    @ApiOperation(value = "This will delete a customer.",
            notes = "Consuming this Api will delete a customer filtered by ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }
}
