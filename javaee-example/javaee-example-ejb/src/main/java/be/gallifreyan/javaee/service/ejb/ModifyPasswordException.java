package be.gallifreyan.javaee.service.ejb;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class ModifyPasswordException extends ApplicationException {
	private static final long serialVersionUID = 236461813446405979L;

	public ModifyPasswordException(String message) {
		super(message);
	}

	public ModifyPasswordException(Set<ConstraintViolation<?>> violations) {
		super(violations);
	}
}
