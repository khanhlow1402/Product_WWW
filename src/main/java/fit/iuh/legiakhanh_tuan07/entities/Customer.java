package fit.iuh.legiakhanh_tuan07.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 3, max = 50, message = "Tên khách hàng phải từ 3-50 ký tự")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Tên chỉ được chứa chữ cái, số và dấu gạch dưới"
    )
    @Column(unique = true, nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "customer_since")
    private Calendar customerSince;

    @Email(message = "Email không hợp lệ")
    @Column(unique = true)
    private String email;

    @Pattern(
            regexp = "^(\\+84|0)[0-9]{9}$",
            message = "Số điện thoại không hợp lệ (VD: 0912345678 hoặc +84912345678)"
    )
    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 200, message = "Địa chỉ không được quá 200 ký tự")
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    // Transient field để validate password khi thêm mới
    @Column(nullable = false)
    private String password;

    private String role;

}