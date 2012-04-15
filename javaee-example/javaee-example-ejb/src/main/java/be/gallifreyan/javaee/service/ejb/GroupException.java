package be.gallifreyan.javaee.service.ejb;

public class GroupException extends ApplicationException
{

	private static final long serialVersionUID = 1L;

	public GroupException(String message)
	{
		super(message);
	}

	public GroupException(Throwable cause)
	{
		super(cause);
	}

}
