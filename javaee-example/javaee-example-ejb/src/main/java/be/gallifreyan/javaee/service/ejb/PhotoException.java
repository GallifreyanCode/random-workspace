package be.gallifreyan.javaee.service.ejb;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class PhotoException extends ApplicationException {
	private static final long serialVersionUID = 4310825557324951365L;

	public PhotoException(String message) {
		super(message);
	}

	public PhotoException(Throwable cause) {
		super(cause);
	}

	public PhotoException(Set<ConstraintViolation<?>> violations) {
		super(violations);
	}
}
