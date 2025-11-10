package fit.iuh.legiakhanh_tuan07.services;


import fit.iuh.legiakhanh_tuan07.entities.Customer;
import fit.iuh.legiakhanh_tuan07.reposities.CustomerReponsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomerReponsitory customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByName(username);

        if (customer == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
        }

        System.out.println("Name: " + customer.getName());
        System.out.println("Password: " + customer.getPassword());
        System.out.println("Role: " + customer.getRole());

        return new User(
                customer.getName(),
                customer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + customer.getRole()))
        );
    }
}
