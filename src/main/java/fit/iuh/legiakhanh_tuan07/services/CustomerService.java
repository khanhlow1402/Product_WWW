package fit.iuh.legiakhanh_tuan07.services;

import fit.iuh.legiakhanh_tuan07.entities.Customer;
import fit.iuh.legiakhanh_tuan07.reposities.CustomerReponsitory;
import fit.iuh.legiakhanh_tuan07.reposities.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class    CustomerService {
    public final CustomerReponsitory customerReponsitory;

    public List<Customer> findAll(){
        return customerReponsitory.findAll();
    }

    public Customer findById(int id){
        return customerReponsitory.findById(id).orElse(null);
    }

    public void upsert(Customer customer){
        if (customer.getRole() == null) {
            customer.setRole("CUSTOMER");
        }
        customerReponsitory.save(customer);
    }

    public Customer findCustomerById(Integer id) {
        return customerReponsitory.findById(id).orElse(null);
    }

    public Customer findCustomerByUsername(String username) {
        return customerReponsitory.findByName(username);
    }

    public boolean existsByUsername(String username) {
        Customer customer = findCustomerByUsername(username);
        return customer != null;
    }
}
