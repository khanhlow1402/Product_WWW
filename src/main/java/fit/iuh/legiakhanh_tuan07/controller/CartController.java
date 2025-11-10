package fit.iuh.legiakhanh_tuan07.controller;

import fit.iuh.legiakhanh_tuan07.entities.*;
import fit.iuh.legiakhanh_tuan07.reposities.CustomerRepository;
import fit.iuh.legiakhanh_tuan07.reposities.OrderLineRepository;
import fit.iuh.legiakhanh_tuan07.reposities.OrderRepository;
import fit.iuh.legiakhanh_tuan07.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private CustomerRepository customerRepository;


    //Xem giỏ hàng
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();

        BigDecimal total = cart.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("total", total);
        return "cart/view";
    }

    //Thêm sản phẩm vào giỏ
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Product product = productService.findById(productId);
        if (product == null || !product.isInStock() || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không hợp lệ hoặc đã hết hàng!");
            return "redirect:/product";
        }

        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();

        cart.merge(productId, new CartItem(product, quantity),
                (oldItem, newItem) -> {
                    oldItem.setQuantity(oldItem.getQuantity() + newItem.getQuantity());
                    return oldItem;
                });

        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm vào giỏ hàng!");
        return "redirect:/product";
    }

    //Xóa sản phẩm khỏi giỏ
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable int productId, HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(productId);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    // Thanh toán → Lưu Order + OrderLines
    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng trống!");
            return "redirect:/cart";
        }

        // Lấy username người đăng nhập
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // "customer" hoặc "admin"

        //Lấy customer tương ứng trong DB
        Customer customer = customerRepository.findByName(username);
        if (customer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy khách hàng: " + username);
            return "redirect:/cart";
        }

        Order order = new Order();
        order.setDate(Calendar.getInstance());
        order.setCustomer(customer);
        orderRepository.save(order);

        for (CartItem item : cart.values()) {
            OrderLine line = new OrderLine();
            line.setOrder(order);
            line.setProduct(item.getProduct());
            line.setAmount(item.getQuantity());
            line.setPurchasePrice(item.getProduct().getPrice());
            orderLineRepository.save(line);
        }

        session.removeAttribute("cart");
        redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công!");
        return "redirect:/product";
    }

}
