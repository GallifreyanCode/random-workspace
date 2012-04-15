package be.gallifreyan.javaee.service.ejb;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class UserException extends ApplicationException {
	private static final long serialVersionUID = 7219122160853191410L;

	/**
	 * 
	 * @param message
	 *            The key in the resource bundle for human-readable messages.
	 */
	public UserException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 *            The cause of the exception
	 */
	public UserException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 *            The key in the resource bundle for human-readable messages.
	 * @param cause
	 *            The cause of the exception
	 */
	public UserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserException(Set<ConstraintViolation<?>> violations) {
		super(violations);
	}
}
