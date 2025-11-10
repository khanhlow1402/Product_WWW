package fit.iuh.legiakhanh_tuan07.services;

import fit.iuh.legiakhanh_tuan07.entities.Category;
import fit.iuh.legiakhanh_tuan07.entities.Comment;
import fit.iuh.legiakhanh_tuan07.entities.Product;
import fit.iuh.legiakhanh_tuan07.reposities.CategoryRepository;
import fit.iuh.legiakhanh_tuan07.reposities.CommentRepository;
import fit.iuh.legiakhanh_tuan07.reposities.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;


    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository, CommentRepository commentRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;

    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        products.forEach(p -> p.getComments().size());
        return products;
    }

    @Transactional(readOnly = true)
    public Product findById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.getComments().size();
        return product;
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return findById(id.intValue());
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(int categoryId) {
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        products.forEach(p -> p.getComments().size());
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategoryName(String categoryName) {
        List<Product> products = productRepository.findByCategory_Name(categoryName);
        products.forEach(p -> p.getComments().size());
        return products;
    }

    @Transactional
    public void saveProduct(Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        if (product.getId() != null) {
            // Cập nhật
            Product existing = productRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + product.getId()));

            existing.setName(product.getName());
            existing.setPrice(product.getPrice());
            existing.setInStock(product.isInStock());
            existing.setCategory(product.getCategory());

            productRepository.save(existing);
        } else {
            // Thêm mới
            productRepository.save(product);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id.intValue());
        productRepository.flush();
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}

