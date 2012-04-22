package be.gallifreyan.javaee.view;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class PageUtilities
{
	public static ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator)
	{
		return new ExpectedCondition<WebElement>()
			{
				public WebElement apply(WebDriver driver)
				{
					WebElement toReturn = driver.findElement(locator);
					if (toReturn.isDisplayed())
					{
						return toReturn;
					}
					return null;
				}
			};
	}
}
