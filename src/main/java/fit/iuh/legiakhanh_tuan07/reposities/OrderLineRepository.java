package fit.iuh.legiakhanh_tuan07.reposities;

import fit.iuh.legiakhanh_tuan07.entities.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
}