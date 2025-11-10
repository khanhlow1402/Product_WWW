package fit.iuh.legiakhanh_tuan07.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 2, message = "Tên sản phẩm phải có ít nhất 2 ký tự")
    @Pattern(
            regexp = "^[A-Z].*$",
            message = "Tên sản phẩm phải bắt đầu bằng chữ in hoa"
    )
    private String name;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.01", message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal price;
    private boolean inStock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    //cai @transient sẽ giúp ko lưu vào db mà chỉ check validate /  do đang xài form product nen khong viet validate ben category
    @Transient
    @Size(min = 2, message = "Tên danh mục phải có ít nhất 2 ký tự")
    @Pattern(regexp = "^[A-Z].*", message = "Chữ cái đầu tiên phải viết hoa")
    private String categoryName;
}
