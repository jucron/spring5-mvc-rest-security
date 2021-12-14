package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.domain.Vendor;
import guru.springframework.domain.security.Level;
import guru.springframework.domain.security.Role;
import guru.springframework.domain.security.User;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import guru.springframework.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;
    private UserService userService;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository, VendorRepository vendorRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        populateCategoryData();
        populateCustomerData();
        populateVendorsData();
        populateUsersData();


        System.out.println("Data Loaded in Categories = " + categoryRepository.count() +
                "\n Data Loaded in Customer = " + customerRepository.count());

    }

    private void populateUsersData() {

        //Creating different roles
        userService.saveRole(new Role(null, Level.USER));
        userService.saveRole(new Role(null, Level.MANAGER));
        userService.saveRole(new Role(null, Level.ADMIN));

        userService.saveUser(new User(null, "John Travolta", "john", "1234", new ArrayList<>()));
        userService.saveUser(new User(null, "Will Smith", "will", "1234", new ArrayList<>()));
        userService.saveUser(new User(null, "Jim Carrey", "jim", "1234", new ArrayList<>()));
        userService.saveUser(new User(null, "Arnold Schwarzenegger", "arnold", "1234", new ArrayList<>()));

        userService.addRoleToUser("john", Level.USER);
        userService.addRoleToUser("john", Level.ADMIN);

        userService.addRoleToUser("will", Level.MANAGER);
        userService.addRoleToUser("will", Level.USER);

        userService.addRoleToUser("jim", Level.USER);
        userService.addRoleToUser("arnold", Level.USER);

    }

    private void populateVendorsData() {
        Vendor vendor1 = new Vendor();
        vendor1.setName("Vendor 1");
        vendorRepository.save(vendor1);

        Vendor vendor2 = new Vendor();
        vendor2.setName("Vendor 2");
        vendorRepository.save(vendor2);
    }

    private void populateCustomerData() {
        Customer john = new Customer();
        john.setFirstname("John");
        john.setLastname("Master");
        Customer larry = new Customer();
        larry.setFirstname("Larry");
        larry.setLastname("Bettencourt");
        Customer fred = new Customer();
        fred.setFirstname("Fred");
        fred.setLastname("Flinstone");
        Customer wilma = new Customer();
        wilma.setFirstname("Wilma");
        wilma.setLastname("Flinstone");


        customerRepository.save(john);
        customerRepository.save(larry);
        customerRepository.save(fred);
        customerRepository.save(wilma);

    }

    private void populateCategoryData() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);
    }
}
