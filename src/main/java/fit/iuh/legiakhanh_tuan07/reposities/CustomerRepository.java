package fit.iuh.legiakhanh_tuan07.reposities;

import fit.iuh.legiakhanh_tuan07.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByName(String name);

}