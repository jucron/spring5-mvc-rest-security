package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        populateCategoryData();
        populateCustomerData();
        populateVendorsData();


        System.out.println("Data Loaded in Categories = " + categoryRepository.count() +
                "\n Data Loaded in Customer = " + customerRepository.count());

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
        Customer jinx = new Customer();
        jinx.setFirstname("Jinx");
        jinx.setLastname("Underworld");
        Customer vi = new Customer();
        vi.setFirstname("Vi");
        vi.setLastname("Underworld");


        customerRepository.save(john);
        customerRepository.save(larry);
        customerRepository.save(jinx);
        customerRepository.save(vi);

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
