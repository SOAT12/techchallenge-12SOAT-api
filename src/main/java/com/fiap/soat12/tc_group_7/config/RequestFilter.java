package com.fiap.soat12.tc_group_7.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.cleanarch.util.JwtTokenUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    @Lazy
    private final SessionToken sessionToken;

    private final JwtTokenUtil jwtTokenUtil;

    //	@Value("${white.list.ipaddress}")
    private String whiteListValue = "";

    //	@Value("${white.list.path}")
    private String pathWhiteListValue = "";

    private Set<String> whiteList = new HashSet<String>();

    private Set<String> pathWhiteList = new HashSet<String>();

    @PostConstruct
    public void init() {

        whiteList.addAll(Arrays.asList(whiteListValue.split(";")));

        pathWhiteList.addAll(Arrays.asList(pathWhiteListValue.split(";")));

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException {

        String path = request.getServletPath();

        try {

            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null) {

                String token = authorizationHeader.substring(7);

                String subject = jwtTokenUtil.getSubject(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null
                        && sessionToken.getTransactions().get(subject) != null) {

                    ObjectMapper mapper = new ObjectMapper();

                    @SuppressWarnings("unchecked")
                    Map<String, Object> claims = mapper.convertValue(jwtTokenUtil.getAllClaimsFromToken(token),
                            Map.class);

                    @SuppressWarnings("unchecked")
                    List<String> authorities = (List<String>) claims.get("authorities");

                    String login = sessionToken.getTransactions().get(subject);

                    UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(login);
                    builder.password(token);
                    builder.authorities(authorities.toArray(new String[0]));

                    UserDetails userDetails = builder.build();

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }

            }

            if (pathWhiteList.contains(path)) {

                WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);

                String userIp = details.getRemoteAddress();

                if (!whiteList.contains(userIp)) {
                    throw new BadCredentialsException("Invalid IP Address: " + userIp);
                }

            }

            chain.doFilter(request, response);

        } catch (Exception e) {
            logger.error(e.getMessage());

            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e);

            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.setStatus(HttpStatus.BAD_REQUEST.value());

            response.getWriter().write(new ObjectMapper().writeValueAsString(apiError));

        }

    }

}
