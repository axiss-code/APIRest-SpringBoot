package com.acme.jfc.exception;

public class ResourceNotFoundException extends RuntimeException {
	
    private static final long serialVersionUID = -2047045858393072648L;

    public ResourceNotFoundException(String itemNotFoundException) {
            super("Elemento no encontrado: " + itemNotFoundException);
    }

}
