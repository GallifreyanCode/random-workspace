package be.gallifreyan.javaee.service.ejb;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class AlbumException extends ApplicationException {
	private static final long serialVersionUID = -744190615541498101L;

	public AlbumException(String message)
	{
		super(message);
	}

	public AlbumException(Throwable cause)
	{
		super(cause);
	}

	public AlbumException(Set<ConstraintViolation<?>> violations)
	{
		super(violations);
	}

}
