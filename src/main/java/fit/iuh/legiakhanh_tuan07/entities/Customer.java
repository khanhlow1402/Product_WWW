package fit.iuh.legiakhanh_tuan07.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Temporal(TemporalType.DATE)
    private Calendar customerSince;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCustomerSince(Calendar customerSince) {
        this.customerSince = customerSince;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Calendar getCustomerSince() {
        return customerSince;
    }
}