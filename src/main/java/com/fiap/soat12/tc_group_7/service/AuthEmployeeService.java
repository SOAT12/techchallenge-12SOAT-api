package com.fiap.soat12.tc_group_7.service;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fiap.soat12.tc_group_7.config.SessionToken;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.util.CryptUtil;
import com.fiap.soat12.tc_group_7.util.JwtTokenUtil;
import com.fiap.soat12.tc_group_7.util.UUIDGeneratorUtil;


@Service
public class AuthEmployeeService implements UserDetailsService {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private SessionToken sessionToken;

	@Lazy
	@Autowired
	private AuthenticationManager authenticationManager;
	    
    private final EmployeeRepository employeeRepository;

	public AuthEmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
    
	public LoginResponseDTO auth(LoginRequestDTO requestDTO) throws Exception {

		try {
			authenticateMobile(requestDTO, false, true);
		} catch (BadCredentialsException e) {

			try {

//				employeeService.authTemporaryPasswordMobile(requestDTO);

				authenticateMobile(requestDTO, true, true);

//				employeeService.authenticatedTemporaryPasswordMobile(requestDTO, true);

			} catch (BadCredentialsException e1) {
				authenticateMobile(requestDTO, false, false);

//				employeeService.authenticatedOldPasswordMobile(requestDTO);

			}

		}

		String uuid = UUIDGeneratorUtil.getInstance().next();

		String[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(item -> item.getAuthority()).toArray(String[]::new);

		Map<String, Object> claims = new HashedMap<>();
		claims.put("authorities", authorities);

		final String token = jwtTokenUtil.generateToken(claims, uuid);

		sessionToken.removeTransactionAndSession(requestDTO.getCpf());
		sessionToken.addTransactionAndSession(requestDTO.getCpf(), uuid);

		LoginResponseDTO loginResponse = new LoginResponseDTO();
		loginResponse.setToken(token);

		return loginResponse;

	}

	private void authenticateMobile(LoginRequestDTO requestDTO, Boolean useTmpPwd, Boolean bcrypt) throws Exception {

		String username = useTmpPwd ? requestDTO.getCpf() + ":USETEMPORARYPASSWORD" : requestDTO.getCpf();

		String password = bcrypt ? requestDTO.getPassword() : CryptUtil.md5(requestDTO.getPassword());

		if (!bcrypt) {
			username += ":MD5";
		}

		try {

			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			Predicate<GrantedAuthority> p1 = s -> s.toString().equals("USETEMPORARYPASSWORD");
			if (authentication.getAuthorities().stream().anyMatch(p1)) {
//				employeeService.authenticatedTemporaryPasswordMobile(loginMobile, false);
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (DisabledException e) {
			throw new BadCredentialsException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", e);
		}

	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String usr = username.split(":")[0];

		Employee usuario = employeeRepository.findByCpf(usr);

		UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(usr);

		String password = username.contains(":USETEMPORARYPASSWORD") ? usuario.getPassword()
				: usuario.getPassword();

		if (username.contains(":MD5")) {
			builder.password(new BCryptPasswordEncoder().encode(password));
		} else {
			builder.password(password);
		}

		return builder.build();

	}
}

