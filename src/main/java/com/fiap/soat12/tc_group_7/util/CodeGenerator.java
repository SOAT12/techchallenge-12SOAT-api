package com.fiap.soat12.tc_group_7.util;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

public class CodeGenerator {
	
	public static Long sequence = 0L;
	
	public static char[] alfabeto = new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	public static String generateCode() {
		
		Calendar now = Calendar.getInstance();
		
		String yy = StringUtils.leftPad(String.valueOf(now.get(Calendar.YEAR)), 4, '0').substring(2);		
		String mm = Character.toString( alfabeto[now.get(Calendar.MONTH)] );
		String dd = String.format("%02X", (0xFF & now.get(Calendar.DAY_OF_MONTH)));
		String hh = Character.toString( alfabeto[now.get(Calendar.HOUR_OF_DAY)] );
		String mi = String.format("%02X", (0xFF & now.get(Calendar.MINUTE)));		
		String ss = String.format("%02X", (0xFF & now.get(Calendar.SECOND)));
		
		String ffff = String.format("%04X", (0xFFFF & CodeGenerator.nextSequence()));
		
		String code = yy + mm + dd + hh + mi + ss + ffff ;
		
		return code;
		
	}
	
	public synchronized static Long nextSequence() {
		
		sequence++;
		
		if ( sequence > 65535L) {
			sequence = 0L;
		}
		
		return sequence;
	}
	
	
}
