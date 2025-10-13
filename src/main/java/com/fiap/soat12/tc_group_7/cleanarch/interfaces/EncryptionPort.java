package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

public interface EncryptionPort {

    String hash(String rawValue);

    boolean checkMatch(String rawValue, String hashedValue);
}