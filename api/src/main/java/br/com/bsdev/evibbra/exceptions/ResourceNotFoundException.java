package br.com.bsdev.evibbra.exceptions;

// Custom exception for resource not found scenarios
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
