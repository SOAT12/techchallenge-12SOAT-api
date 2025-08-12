package com.fiap.soat12.tc_group_7.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptUtil {

    private CryptUtil() {
    }

    public static String md5(String value)  {
        String sen = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, md.digest(value.getBytes()));
            sen = hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Não foi possível encontrar o algoritmo MD5.", e);
        }
        return sen;
    }
	
	public static String bcrypt(String value) {
		
		return new BCryptPasswordEncoder().encode(value);

	}

}
