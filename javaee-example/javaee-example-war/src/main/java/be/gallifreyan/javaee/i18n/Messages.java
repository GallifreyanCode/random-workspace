package be.gallifreyan.javaee.i18n;

import java.util.*;

public class Messages
{
	private static final String BUNDLE_NAME = "resources.messages"; //$NON-NLS-1$

	private static final ResourceBundle DEFAULT_RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages()
	{
	}

	public static String getString(String key, Locale locale)
	{
		try
		{
			ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
			return bundle.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	public static String getLoggerString(String key)
	{
		try
		{
			return DEFAULT_RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}
}
