package fit.iuh.legiakhanh_tuan07.reposities;

import fit.iuh.legiakhanh_tuan07.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Kế thừa JpaRepository để có sẵn các phương thức CRUD
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory_Id(Integer categoryId);

    // 2. Hoặc nếu bạn muốn tìm theo tên category
    List<Product> findByCategory_Name(String categoryName);
}