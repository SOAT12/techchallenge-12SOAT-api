package com.fiap.soat12.tc_group_7.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CryptUtil {
	
	public static String md5(String value){  
        String sen = "";  
        MessageDigest md = null;  
        try {  
            md = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        BigInteger hash = new BigInteger(1, md.digest(value.getBytes()));  
        sen = hash.toString(16);              
        return sen;  
    }
	
	public static String bcrypt(String value) {
		
		return new BCryptPasswordEncoder().encode(value);

	}

}
