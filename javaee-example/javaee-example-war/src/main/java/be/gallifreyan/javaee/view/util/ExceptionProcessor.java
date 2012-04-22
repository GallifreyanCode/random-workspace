package be.gallifreyan.javaee.view.util;

import java.util.*;

import javax.faces.application.*;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.validation.*;
import javax.validation.MessageInterpolator.Context;
import javax.validation.metadata.ConstraintDescriptor;

import be.gallifreyan.javaee.i18n.Messages;
import be.gallifreyan.javaee.service.ejb.ApplicationException;

/**
 * A utility class to process exceptions. The class may populate the contents of
 * {@link FacesMessage} instances and add them to the {@link FacesContext} so
 * that the contents of the exception messages can be displayed to end-users.
 * 
 * @author Vineet Reynolds
 * 
 */
public class ExceptionProcessor
{
	private ExceptionProcessor()
	{
	}

	/**
	 * Parse an {@link ApplicationException} instance and extracts the
	 * associated {@link ConstraintViolation} and {@link Exception#getMessage()}
	 * . The message associated with the {@link ConstraintViolation} is
	 * localized before creation of an associated {@link FacesMessage} that
	 * describes the {@link ConstraintViolation}. The
	 * {@link Exception#getMessage()} is expected to return a key in
	 * {@link ResourceBundle} that is then used to lookup the localized message
	 * in the {@link ResourceBundle}.
	 * 
	 * The {@link FacesMessage} instances that are populated into the
	 * {@link FacesContext} instance have a {@link Severity} of
	 * {@link FacesMessage#SEVERITY_ERROR} by design. Also, the
	 * {@link FacesMessage} instances are global by design.
	 * 
	 * @param exception
	 *            The ApplicationException instance to be processed.
	 */
	public static void populateErrorMessage(ApplicationException exception)
	{
		ValidatorFactory defaultValidatorFactory = Validation.buildDefaultValidatorFactory();
		MessageInterpolator interpolator = defaultValidatorFactory.getMessageInterpolator();

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (exception.getViolations() != null)
		{
			for (final ConstraintViolation<?> violation : exception.getViolations())
			{
				String message = interpolator.interpolate(violation.getMessageTemplate(), new Context()
					{

						@Override
						public Object getValidatedValue()
						{
							return violation.getInvalidValue();
						}

						@Override
						public ConstraintDescriptor<?> getConstraintDescriptor()
						{
							return violation.getConstraintDescriptor();
						}
					}, locale);

				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			}
		}

		String exceptionMessage = exception.getMessage();
		if (exceptionMessage != null && !exceptionMessage.trim().equals(""))
		{
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.getString(
					exceptionMessage, locale), null);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}
}
