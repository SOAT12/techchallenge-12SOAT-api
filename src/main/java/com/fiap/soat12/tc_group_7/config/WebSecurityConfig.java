package com.fiap.soat12.tc_group_7.config;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter.JwtUserDetailsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {

    private static final String[] AUTHORIZED_ROLES = {"GESTOR", "ATENDENTE", "MECÂNICO"};
    private static final String STOCK_ID_PATH = "/api/stock/{id}";
    private static final String EMPLOYEE_ID_PATH = "/api/employees/{id}";
    private static final String EMPLOYEE_FUNCTION_ID_PATH = "/api/employee-functions/{id}";
    private static final String SERVICE_ORDER_ID_PATH = "/api/service-orders/{id}";
    private static final String TOOL_CATEGORIES_PATH = "/api/tool-categories";
    private static final String VEHICLE_ID_PATH = "/api/vehicle/{id}";
    private static final String TOOL_CATEGORIES_BASE_PATH = "/api/tool-categories";

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtUserDetailsAdapter jwtUserDetailsService;
    private final RequestFilter jwtRequestFilter;

    public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             JwtUserDetailsAdapter jwtUserDetailsService,
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
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
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
                                "/api/auth/login",
                                "/api/auth/forgot-password",
                                "/api/service-orders/1/webhook/approval"
                        ).permitAll()
                        .requestMatchers("/actuator/health/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/employees").permitAll()

                        // Endpoints do StockController
                        .requestMatchers(HttpMethod.POST, "/api/stock").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, STOCK_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/stock/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/stock").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, STOCK_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, STOCK_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/stock/{id}/reactivate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do EmployeeController
                        .requestMatchers(HttpMethod.GET, EMPLOYEE_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employees/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, EMPLOYEE_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, EMPLOYEE_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}/activate").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employees/{id}/change-password").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do EmployeeFunctionController
                        .requestMatchers(HttpMethod.POST, "/api/employee-functions").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, EMPLOYEE_FUNCTION_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/employee-functions/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, EMPLOYEE_FUNCTION_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, EMPLOYEE_FUNCTION_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/employee-functions/{id}/activate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do ServiceOrderController
                        .requestMatchers(HttpMethod.POST, "/api/service-orders").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.POST, "/api/service-orders/full").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, SERVICE_ORDER_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/service-orders").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/service-orders/status").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, SERVICE_ORDER_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, SERVICE_ORDER_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/diagnose").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/wait-for-approval").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/approve").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/reject").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/finish").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/service-orders/{id}/deliver").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do ToolCategoryController
                        .requestMatchers(HttpMethod.POST, TOOL_CATEGORIES_BASE_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, TOOL_CATEGORIES_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/tool-categories/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, TOOL_CATEGORIES_BASE_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, TOOL_CATEGORIES_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, TOOL_CATEGORIES_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/tool-categories/{id}/reactivate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do VehicleServiceController
                        .requestMatchers(HttpMethod.GET, "/api/vehicle-services").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle-services/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.POST, "/api/vehicle-services").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle-services/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PATCH, "/api/vehicle-services/{id}/deactivate").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do CustomerController
                        .requestMatchers(HttpMethod.GET, "/api/customers/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/customers/cpf").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.POST, "/api/customers").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/customers/{id}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, "/api/customers/{id}").hasAnyRole(AUTHORIZED_ROLES)

                        // Endpoints do VehicleController
                        .requestMatchers(HttpMethod.POST, "/api/vehicle").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, VEHICLE_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/plate/{licensePlate}").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/all").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.GET, "/api/vehicle").hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.PUT, VEHICLE_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
                        .requestMatchers(HttpMethod.DELETE, VEHICLE_ID_PATH).hasAnyRole(AUTHORIZED_ROLES)
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