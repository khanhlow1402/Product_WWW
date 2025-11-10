package fit.iuh.legiakhanh_tuan07.services;


import fit.iuh.legiakhanh_tuan07.entities.Category;
import fit.iuh.legiakhanh_tuan07.reposities.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
    @Service
    public class CategoryService {
        private final CategoryRepository categoryRepository;
        public CategoryService(CategoryRepository categoryRepository) {
            this.categoryRepository = categoryRepository;
        }
        public List<Category> findAll() {
            return categoryRepository.findAll();
        }

        public Category findByName(String name) {
            return categoryRepository.findByName(name);
        }
        public Category findById(Integer id) {
            return categoryRepository.findById(id).orElse(null);
        }
        public void save(Category category) {
            categoryRepository.save(category);
        }
    }