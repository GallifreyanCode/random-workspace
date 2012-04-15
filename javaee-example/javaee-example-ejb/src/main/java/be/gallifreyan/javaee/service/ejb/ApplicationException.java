package be.gallifreyan.javaee.service.ejb;

import java.util.Set;

import javax.validation.ConstraintViolation;

@javax.ejb.ApplicationException(rollback = true)
public class ApplicationException extends Exception
{

	private static final long serialVersionUID = 1L;
	private Set<ConstraintViolation<?>> violations;

	public ApplicationException()
	{
	}

	/**
	 * @param message
	 */
	public ApplicationException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public ApplicationException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ApplicationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ApplicationException(Set<ConstraintViolation<?>> violations)
	{
		this.violations = violations;
	}

	public Set<ConstraintViolation<?>> getViolations()
	{
		return violations;
	}

	public void setViolations(Set<ConstraintViolation<?>> violations)
	{
		this.violations = violations;
	}

}
