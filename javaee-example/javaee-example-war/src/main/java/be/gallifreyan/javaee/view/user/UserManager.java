package be.gallifreyan.javaee.view.user;

import java.util.*;

import javax.ejb.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.*;

import be.gallifreyan.javaee.entity.User;
import be.gallifreyan.javaee.i18n.Messages;
import be.gallifreyan.javaee.service.ejb.ModifyPasswordException;
import be.gallifreyan.javaee.service.ejb.ModifyPasswordRequest;
import be.gallifreyan.javaee.service.ejb.UserException;
import be.gallifreyan.javaee.service.ejb.UserService;
import be.gallifreyan.javaee.service.ejb.ApplicationException;
import be.gallifreyan.javaee.view.util.ExceptionProcessor;

@ManagedBean
@ViewScoped
public class UserManager
{

	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

	private SignupRequest signupRequest = new SignupRequest();

	private ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest();

	@EJB
	private UserService userAccountService;

	public SignupRequest getSignupRequest()
	{
		return signupRequest;
	}

	public void setSignupRequest(SignupRequest signupRequest)
	{
		this.signupRequest = signupRequest;
	}

	public ModifyPasswordRequest getModifyPasswordRequest()
	{
		return modifyPasswordRequest;
	}

	public void setModifyPasswordRequest(ModifyPasswordRequest modifyPasswordRequest)
	{
		this.modifyPasswordRequest = modifyPasswordRequest;
	}

	public String signupUser()
	{
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		String result = null;
		if (!Arrays.equals(signupRequest.getPassword(), signupRequest.getConfirmPassword()))
		{
			String key = "Signup.PasswordMismatchMessage";
			logger.error(Messages.getLoggerString(key));
			String message = Messages.getString(key, locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			return result;
		}

		try
		{
			User user = new User(signupRequest.getUserId(), signupRequest.getPassword());
			userAccountService.signupUser(user);
			String message = Messages.getString("Signup.AccountCreationSuccessMessage", locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			result = "/Index.xhtml?faces-redirect=true";
		}
		catch (UserException userEx)
		{
			logger.error(Messages.getLoggerString("Signup.AccountCreationFailureMessage"), userEx);
			populateErrorMessage(userEx);
		}
		catch (EJBException ejbEx)
		{
			String key = "Signup.InternalFailureMessage";
			logger.error(Messages.getLoggerString(key), ejbEx);
			String message = Messages.getString(key, locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
		return result;
	}

	public String modifyPassword()
	{
		String result = null;
		FacesContext context = FacesContext.getCurrentInstance();
		Locale locale = context.getViewRoot().getLocale();
		try
		{
			userAccountService.modifyPassword(modifyPasswordRequest);
			String message = Messages.getString("AccountPreferences.PasswordModificationSuccessMessage", locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
			context.addMessage(null, facesMessage);
			result = "/private/HomePage.xhtml?faces-redirect=true";
		}
		catch (ModifyPasswordException modifyEx)
		{
			logger.error(Messages.getLoggerString("Signup.AccountCreationFailureMessage"), modifyEx);
			populateErrorMessage(modifyEx);
		}
		catch (EJBException ejbEx)
		{
			String key = "AccountPreferences.InternalModifyErrorMessage";
			logger.error(Messages.getLoggerString(key), ejbEx);
			String message = Messages.getString(key, locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
			context.addMessage(null, facesMessage);
		}
		return result;
	}

	public String deleteAccount()
	{
		String result = null;
		FacesContext context = FacesContext.getCurrentInstance();
		Locale locale = context.getViewRoot().getLocale();
		try
		{
			userAccountService.deleteUserAccount();
			String message = Messages.getString("AccountPreferences.AccountDeleteSuccessMessage", locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
			context.addMessage(null, facesMessage);
			result = "/Index.xhtml?faces-redirect=true";
			ExternalContext externalContext = context.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
			request.logout();
		}
		catch (UserException userEx)
		{
			String key = "AccountPreferences.InternalDeleteErrorMessage";
			logger.error(Messages.getLoggerString(key), userEx);
			String message = Messages.getString(key, locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
			context.addMessage(null, facesMessage);
		}
		catch (EJBException ejbEx)
		{
			String key = "AccountPreferences.InternalDeleteErrorMessage";
			logger.error(Messages.getLoggerString(key), ejbEx);
			String message = Messages.getString(key, locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
			context.addMessage(null, facesMessage);
		}
		catch (ServletException servletEx)
		{
			String key = "AccountPreferences.ClearCookieCloseBrowserMessage";
			logger.error(Messages.getLoggerString(key), servletEx);
			String message = Messages.getString(key, locale);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, message, null);
			context.addMessage(null, facesMessage);
		}
		return result;
	}

	private void populateErrorMessage(ApplicationException exception)
	{
		ExceptionProcessor.populateErrorMessage(exception);
	}

}
