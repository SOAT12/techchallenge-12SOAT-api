package com.fiap.soat12.tc_group_7.exception;

public class BadCredentialsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String userMessage = "CREDENCIAL INVALIDA!"; 

    public BadCredentialsException() {
    	super(userMessage);
    }

    public BadCredentialsException(String message) {
    	super(userMessage + ": " + message);
    }

    public BadCredentialsException(String message, Throwable cause) {
    	 super(userMessage + ": " + message, cause);
    }

    public BadCredentialsException(Throwable cause) {
    	super(cause);
    }
	 
}



