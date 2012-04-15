package be.gallifreyan.javaee.service.ejb;

public class GroupException extends ApplicationException {
	private static final long serialVersionUID = 6543664691465582518L;

	public GroupException(String message) {
		super(message);
	}

	public GroupException(Throwable cause) {
		super(cause);
	}
}
