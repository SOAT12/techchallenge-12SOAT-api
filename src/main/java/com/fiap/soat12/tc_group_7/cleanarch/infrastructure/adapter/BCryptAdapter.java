package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter;

import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EncryptionPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptAdapter implements EncryptionPort {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String rawValue) {
        return passwordEncoder.encode(rawValue);
    }

    @Override
    public boolean checkMatch(String rawValue, String hashedValue) {
        return passwordEncoder.matches(rawValue, hashedValue);
    }
}