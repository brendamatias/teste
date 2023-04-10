package br.com.bsdev.evibbra.exceptions;

// Custom exception for invalid status scenarios
public class InvalidStatusException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidStatusException(String message) {
		super(message);
	}
}