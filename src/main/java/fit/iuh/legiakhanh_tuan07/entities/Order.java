package fit.iuh.legiakhanh_tuan07.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Calendar date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<OrderLine> orderLines;

    // Phương thức này bị lỗi vì không thấy getter của OrderLine
    // Cách viết khác để giải quyết lỗi Incompatible Types:
    public BigDecimal getTotalAmount() {
        if (orderLines == null) return BigDecimal.ZERO;
        return orderLines.stream()
                .map(line -> line.getPurchasePrice().multiply(new BigDecimal(line.getAmount())))
                // HOẶC
                // .map(line -> line.getPurchasePrice().multiply(BigDecimal.valueOf(line.getAmount().longValue()))) // Nếu amount là Integer
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}