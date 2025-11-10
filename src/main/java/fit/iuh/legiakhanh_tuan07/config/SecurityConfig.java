package fit.iuh.legiakhanh_tuan07.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Tạo sẵn 2 tài khoản mẫu:
     *  - admin / 123
     *  - customer / 111
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails customer = User.builder()
                .username("customer")
                .password(passwordEncoder().encode("111"))
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, customer);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF hoàn toàn (dev/test)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Ai cũng truy cập được
                        .requestMatchers("/", "/home", "/register",
                                "/css/**", "/js/**", "/images/**").permitAll()

                        // Xem và lọc sản phẩm: ai cũng truy cập được
                        .requestMatchers("/product", "/product/detail/**", "/product/category").permitAll()

                        // Chat với AI: ai cũng dùng được
                        .requestMatchers("/chat/**").permitAll()

                        // Mua sản phẩm: customer, admin mới được mua
                        .requestMatchers("/order/buy/**").hasAnyRole("ADMIN", "CUSTOMER")
                        // Chỉ Customer, admin mới được mua hoặc xem order
                        .requestMatchers("/order/**", "/cart/**").hasAnyRole("CUSTOMER","ADMIN")

                        // Chỉ Admin mới được thêm/sửa/xóa sản phẩm, quản lý user
                        .requestMatchers("/product/add", "/product/edit/**",
                                "/product/delete/**", "/customer/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // xài form mặc định của security
                .formLogin(form -> form
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

}
