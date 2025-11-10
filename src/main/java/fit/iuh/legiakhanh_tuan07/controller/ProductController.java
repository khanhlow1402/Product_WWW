package fit.iuh.legiakhanh_tuan07.controller;


import fit.iuh.legiakhanh_tuan07.entities.Category;
import fit.iuh.legiakhanh_tuan07.entities.Comment;
import fit.iuh.legiakhanh_tuan07.entities.Product;
import fit.iuh.legiakhanh_tuan07.services.CategoryService;
import fit.iuh.legiakhanh_tuan07.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // 🔹 Danh sách sản phẩm (Guest + Customer + Admin)
    @GetMapping
    public String showProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategoryId", null);
        return "product/list";
    }

    // 🔹 Chi tiết sản phẩm – ai cũng xem được
    @GetMapping("/detail/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model) {
        Product p = productService.findById(id);
        model.addAttribute("product", p);
        return "product/productdetail";
    }

    // Thêm sản phẩm – chỉ admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("isEdit", false);
        return "product/productform";
    }

    // Lưu sản phẩm – chỉ admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Integer categoryId,
            Model model
    ) {
        //  debug
        System.out.println("=== [DEBUG] /product/save called ===");
        System.out.println("Product ID: " + product.getId());
        System.out.println("Product Name: " + product.getName());
        System.out.println("Price: " + product.getPrice());
        System.out.println("In Stock: " + product.isInStock());
        System.out.println("Category ID: " + categoryId);
        System.out.println("Category Name: " + categoryName);


        if (result.hasErrors()) {
            System.out.println("Validation errors found:");
            result.getAllErrors().forEach(error -> System.out.println(" - " + error.toString()));

            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("isEdit", product.getId() != null);
            return "product/productform";
        }

        Category category = null;

        if (product.getId() == null) {
            // add
            if (categoryName != null && !categoryName.trim().isEmpty()) {
                category = categoryService.findByName(categoryName);
                if (category == null) {
                    category = new Category();
                    category.setName(categoryName);
                    categoryService.save(category);
                    System.out.println(" Created new category: " + category.getName());
                }
            }
        } else {
            // edit
            if (categoryId != null) {
                category = categoryService.findById(categoryId);
                System.out.println("Using selected category: " + category.getName());
            } else {
                Product oldProduct = productService.findById(product.getId());
                category = oldProduct.getCategory();
                System.out.println("Kept old category: " + (category != null ? category.getName() : "null"));
            }
        }

        product.setCategory(category);

        System.out.println("💾 Saving product: " + product.getName());
        productService.saveProduct(product);
        System.out.println("Product saved successfully, redirecting...");
        return "redirect:/product";
    }



    // Sửa sản phẩm – chỉ admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("isEdit", true);
        return "product/productform";
    }
    // 🔹 Xóa sản phẩm – chỉ ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xoá sản phẩm thành công!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xoá sản phẩm vì đã có đơn hàng chứa sản phẩm này!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/product";
    }


    // 🔹 Lọc sản phẩm theo danh mục
    @GetMapping("/category")
    public String filterByCategory(@RequestParam(required = false) Integer categoryId, Model model) {
        List<Product> products;

        if (categoryId != null && categoryId > 0) {
            products = productService.findByCategoryId(categoryId);
        } else {
            products = productService.findAll();
        }

        List<Category> categories = categoryService.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);

        return "product/list";
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable("id") int productId,
                             @RequestParam("text") String text) {
        Product product = productService.findById(productId);

        Comment comment = new Comment();
        comment.setText(text);
        comment.setProduct(product);
        // Lưu comment
        productService.saveComment(comment);
        // Redirect
        return "redirect:/product/detail/" + productId;
    }
}

