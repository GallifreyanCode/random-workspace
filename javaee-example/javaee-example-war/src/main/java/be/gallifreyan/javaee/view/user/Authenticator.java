package be.gallifreyan.javaee.view.user;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.*;

import be.gallifreyan.javaee.i18n.Messages;

@ManagedBean
@RequestScoped
public class Authenticator
{
	private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);

	private String userId;
	private char[] password;

	public Authenticator()
	{
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public char[] getPassword()
	{
		return password;
	}

	public void setPassword(char[] password)
	{
		this.password = password;
	}

	public String authenticate()
	{
		String result = null;
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		try
		{
			request.login(userId, new String(password));
			result = "/private/HomePage.xhtml?faces-redirect=true";
		}
		catch (ServletException ex)
		{
			logger.error("Failed to authenticate user.", ex);
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.getString(
					"Login.InvalidIdPasswordMessage", locale), null);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
		return result;
	}

	public String logout()
	{
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		try
		{
			request.logout();
		}
		catch (ServletException servletEx)
		{
			logger.warn("Failed to logout the user", servletEx);
		}
		return "/Login.xhtml?faces-redirect=true";
	}

}