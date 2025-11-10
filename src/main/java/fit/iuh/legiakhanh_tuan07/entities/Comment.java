package fit.iuh.legiakhanh_tuan07.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments") // Tên bảng trong CSDL MariaDB: comments
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // id: Integer [cite: 39]
    private String text; // text: String [cite: 39]

    // Many-to-One relationship với Product [cite: 6]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // Khóa ngoại liên kết với Product
    private Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}