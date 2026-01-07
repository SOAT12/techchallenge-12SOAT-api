package com.fiap.soat12.tc_group_7.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;

import static com.fiap.soat12.tc_group_7.config.WebSecurityConfig.routesProtectedByLambdaAuth;

@Component
@RequiredArgsConstructor
public class LambdaRequestFilter extends OncePerRequestFilter {

    private static final String SECRET = "my-super-secret-key-for-hs256-test-only";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String path = request.getServletPath();

        if (routesProtectedByLambdaAuth.stream().anyMatch(pattern -> pathMatcher.match(pattern, path))) {
            try {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new ServletException("Missing or invalid Authorization header");
                }

                String token = authHeader.substring(7);

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);

                Claims claims = claimsJws.getBody();

                if (!"auth-lambda".equals(claims.getIssuer()) ||
                        !"techchallenge-12SOAT".equals(claims.getAudience()) ||
                        !"api:access".equals(claims.get("scope"))) {
                    throw new ServletException("Invalid token claims");
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                claims.getSubject(), null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | ServletException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"Unauthorized: " + e.getMessage() + "\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
