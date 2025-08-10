package com.fiap.soat12.tc_group_7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Import this
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Import this
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false) // debug can be configured directly in application.properties or .yml if preferred
public class WebSecurityConfig {

    // Use constructor injection for dependencies
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService jwtUserDetailsService;
    private final RequestFilter jwtRequestFilter;

    public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             UserDetailsService jwtUserDetailsService,
                             RequestFilter jwtRequestFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // Mark as public
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() { // Mark as public
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Uses the passwordEncoder bean defined above
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/api/employees/login",
                                "/api/employees/forgot-password"
                        ).permitAll()

                        // Endpoints do StockController
                        .requestMatchers(HttpMethod.POST, "/api/stock").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/stock/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/stock/all").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/stock").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/stock/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/stock/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/stock/{id}/reactivate").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        // Endpoints do EmployeeController
                        .requestMatchers(HttpMethod.POST, "/api/employees").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/employees/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/employees/all").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}/activate").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}/change-password").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        // Endpoints do EmployeeFunctionController
                        .requestMatchers(HttpMethod.POST, "/api/employee-functions").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions/all").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/employee-functions/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/employee-functions/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/employee-functions/{id}/activate").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        // Endpoints do ServiceOrderController
                        .requestMatchers(HttpMethod.POST, "/api/service-orders").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/service-orders/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/service-orders").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/service-orders/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/service-orders/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/diagnose").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/wait-for-approval").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/approve").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/reject").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/finish").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/deliver").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        // Endpoints do ToolCategoryController
                        .requestMatchers(HttpMethod.POST, "/api/tool-categories").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/tool-categories/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/tool-categories/all").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/tool-categories").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/tool-categories/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/tool-categories/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/tool-categories/{id}/reactivate").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        // Endpoints do VehicleServiceController
                        .requestMatchers(HttpMethod.GET, "/v1/vehicle-services").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/v1/vehicle-services/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.POST, "/v1/vehicle-services").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/v1/vehicle-services/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/v1/vehicle-services/{id}/deactivate").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        // Endpoints do CustomerController
                        .requestMatchers(HttpMethod.GET, "/v1/customers").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/v1/customers/cpf").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.POST, "/v1/customers").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/v1/customers/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.DELETE, "/v1/customers/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        // Endpoints do VehicleController
                        .requestMatchers(HttpMethod.POST, "/api/vehicle").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/plate/{licensePlate}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/all").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.GET, "/api/vehicle").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle/{id}").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/vehicle/{id}/reactivate").hasAnyRole("GESTOR", "ATENDENTE", "MECANICO")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                
                .build();
        
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // Mark as public
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }
}