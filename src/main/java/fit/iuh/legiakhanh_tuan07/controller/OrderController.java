package fit.iuh.legiakhanh_tuan07.controller;

import fit.iuh.legiakhanh_tuan07.entities.Customer;
import fit.iuh.legiakhanh_tuan07.entities.Order;
import fit.iuh.legiakhanh_tuan07.services.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    //xem chi tiet hd
    @GetMapping("/{id}")
    public String showOrder(@PathVariable int id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order/orderdetail";
    }
    //mua hang
    @PostMapping("/buy")
    public String buyProduct(@RequestParam("productId") int productId, Principal principal) {

        // Nếu chưa đăng nhập (guest)
        if (principal == null) {
            return "redirect:/login";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isCustomer = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

        Customer customer = new Customer();
        if (isAdmin) {
            customer.setName("admin");
        } else if (isCustomer) {
            customer.setName("customer");
        } else {
            // Guest thì redirect tới login
            return "redirect:/login";
        }

        orderService.createOrder(productId, customer);
        return "redirect:/order/success";
    }
    @GetMapping("/success")
    public String orderSuccess() {
        return "order/success";
    }
    //xem hoa don
    @GetMapping
    public String showAllOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<Order> orderList;

        // Nếu là admin → xem tất cả
        if ("admin".equalsIgnoreCase(username)) {
            orderList = orderService.findAllOrders();
        }
        // Nếu là customer → chỉ xem đơn của chính mình
        else if ("customer".equalsIgnoreCase(username)) {
            orderList = orderService.findByCustomerName("customer");
        }
        else {
            orderList = List.of();
        }

        model.addAttribute("orders", orderList);
        return "order/list";
    }

}