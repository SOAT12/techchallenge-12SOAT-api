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
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {

    private static final String[] AUTHORIZED_ROLES = {"GESTOR", "ATENDENTE", "MECANICO"};

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
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/api/employees/login",
                                "/api/employees/forgot-password",
                                "/api/employees"
                        ).permitAll()

                        // Endpoints do StockController
                        .requestMatchers(HttpMethod.POST, "/api/stock").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/stock/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/stock/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/stock").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/stock/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/api/stock/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/stock/{id}/reactivate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do EmployeeController
                        .requestMatchers(HttpMethod.POST, "/api/employees").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employees/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employees/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}/activate").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}/change-password").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do EmployeeFunctionController
                        .requestMatchers(HttpMethod.POST, "/api/employee-functions").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employee-functions/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/api/employee-functions/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employee-functions/{id}/activate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do ServiceOrderController
                        .requestMatchers(HttpMethod.POST, "/api/service-orders").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/service-orders/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/service-orders").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/api/service-orders/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/service-orders/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/diagnose").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/wait-for-approval").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/approve").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/reject").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/finish").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/deliver").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do ToolCategoryController
                        .requestMatchers(HttpMethod.POST, "/api/tool-categories").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/tool-categories/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/tool-categories/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/tool-categories").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/tool-categories/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/api/tool-categories/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/tool-categories/{id}/reactivate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do VehicleServiceController
                        .requestMatchers(HttpMethod.GET, "/v1/vehicle-services").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/v1/vehicle-services/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.POST, "/v1/vehicle-services").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/v1/vehicle-services/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/v1/vehicle-services/{id}/deactivate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do CustomerController
                        .requestMatchers(HttpMethod.GET, "/v1/customers").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/v1/customers/cpf").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.POST, "/v1/customers").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/v1/customers/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/v1/customers/{id}").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do VehicleController
                        .requestMatchers(HttpMethod.POST, "/api/vehicle").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/plate/{licensePlate}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/vehicle/{id}/reactivate").hasAnyRole(AUTHORIZED_ROLES)

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