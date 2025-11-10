package fit.iuh.legiakhanh_tuan07.controller;

import fit.iuh.legiakhanh_tuan07.entities.Customer;
import fit.iuh.legiakhanh_tuan07.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/customer")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Hiển thị danh sách khách hàng
    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    // Hiển thị form thêm khách hàng
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("isEdit", false);
        return "customer/form";
    }

    // Lưu khách hàng mới
//    @PostMapping("/save")
//    public String saveCustomer(
//            @Valid @ModelAttribute("customer") Customer customer,
//            @RequestParam(required = false) String password,
//            BindingResult result,
//            Model model,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (result.hasErrors()) {
//            model.addAttribute("isEdit", customer.getId() != null);
//            return "customer/form";
//        }
//
//        try {
//            if (customer.getId() == null) {
//                // Thêm mới
//                customer.setCustomerSince(Calendar.getInstance());
//                customerService.createCustomerWithCredentials(customer, password);
//                redirectAttributes.addFlashAttribute("successMessage", "Thêm khách hàng thành công!");
//            } else {
//                // Cập nhật
//                customerService.updateCustomer(customer, password);
//                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật khách hàng thành công!");
//            }
//            return "redirect:/customer";
//        } catch (RuntimeException e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            model.addAttribute("isEdit", customer.getId() != null);
//            return "customer/form";
//        }
//    }

    // Hiển thị form sửa khách hàng
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return "redirect:/customer";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("isEdit", true);
        return "customer/form";
    }

    // Xem chi tiết khách hàng
    @GetMapping("/detail/{id}")
    public String showCustomerDetail(@PathVariable Integer id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return "redirect:/customer";
        }
        model.addAttribute("customer", customer);
        return "customer/detail";
    }
}