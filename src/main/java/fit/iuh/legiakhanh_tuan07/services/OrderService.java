package fit.iuh.legiakhanh_tuan07.services;


import fit.iuh.legiakhanh_tuan07.entities.Customer;
import fit.iuh.legiakhanh_tuan07.entities.Order;
import fit.iuh.legiakhanh_tuan07.entities.OrderLine;
import fit.iuh.legiakhanh_tuan07.entities.Product;
import fit.iuh.legiakhanh_tuan07.reposities.CustomerRepository;
import fit.iuh.legiakhanh_tuan07.reposities.OrderRepository;
import fit.iuh.legiakhanh_tuan07.reposities.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {
 
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // Theo dõi lịch sử mua hàng và lịch sử giao dịch của khách hàng
    @Transactional(readOnly = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    // Ghi lại thông tin đơn hàng chi tiết
    @Transactional(readOnly = true)
    public Order findById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hóa đơn: " + id));
        order.getOrderLines().size();
        return order;
    }
    @Transactional
    public void createOrder(int productId, Customer customerInput) {
        //  Tìm sản phẩm
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + productId));


        Customer customer = new Customer();
        customer.setName(customerInput.getName());
        customer.setCustomerSince(Calendar.getInstance());
        customerRepository.save(customer);

        // Tạo đơn hàng
        Order order = new Order();
        order.setCustomer(customer);
        order.setDate(Calendar.getInstance());

        // Tạo  chi tiết đơn hàng
        OrderLine line = new OrderLine();
        line.setOrder(order);
        line.setProduct(product);
        line.setAmount(1);
        line.setPurchasePrice(product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);

        // Gắn OrderLine zô Order
        order.setOrderLines(Collections.singleton(line));

        //  Lưu
        orderRepository.save(order);
    }
    @Transactional(readOnly = true)
    public List<Order> findByCustomerName(String name) {
        return orderRepository.findAll()
                .stream()
                .filter(o -> o.getCustomer() != null && name.equalsIgnoreCase(o.getCustomer().getName()))
                .toList();
    }

}