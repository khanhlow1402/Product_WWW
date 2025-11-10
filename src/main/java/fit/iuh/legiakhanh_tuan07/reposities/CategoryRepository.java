package fit.iuh.legiakhanh_tuan07.reposities;

import fit.iuh.legiakhanh_tuan07.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Ví dụ: tìm danh mục theo tên
    Category findByName(String name);
}
