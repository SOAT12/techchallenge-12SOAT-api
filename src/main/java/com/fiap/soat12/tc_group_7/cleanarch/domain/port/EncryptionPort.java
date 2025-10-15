package com.fiap.soat12.tc_group_7.cleanarch.domain.port;

public interface EncryptionPort {

    String hash(String rawValue);

    boolean checkMatch(String rawValue, String hashedValue);
}