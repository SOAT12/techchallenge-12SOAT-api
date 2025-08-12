package com.fiap.soat12.tc_group_7.util;

import com.fasterxml.uuid.Generators;

public class UUIDGeneratorUtil {
	
	private static UUIDGeneratorUtil instance = null;

	private String uuid;
	
	private UUIDGeneratorUtil() {
		
		uuid = "";
		
	}
	
	public static UUIDGeneratorUtil getInstance(){
		
		if ( instance == null ){
			instance = new UUIDGeneratorUtil();
		}
		
		return instance;
	}
	
	public synchronized String next(){
		
		uuid = Generators.timeBasedGenerator().generate().toString();
		
		return uuid;

	}
}
