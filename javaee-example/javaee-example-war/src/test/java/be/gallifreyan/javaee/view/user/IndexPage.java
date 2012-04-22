package be.gallifreyan.javaee.view.user;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;

public class IndexPage
{
	private WebDriver driver;

	private URI contextPath;

	private static final String PAGE_FILE_NAME = "Index.xhtml";

	public IndexPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("publicWrapper")));
		if (!driver.getCurrentUrl().equals(contextPath.toString())
				&& !driver.getCurrentUrl().equals(contextPath + PAGE_FILE_NAME))
		{
			throw new IllegalStateException("This is not the Index page.");
		}
		if (!driver.getTitle().equals("Welcome to Galleria"))
		{
			throw new IllegalStateException("This is not the Index page.");
		}
	}

	public SignupPage chooseToSignup()
	{
		driver.findElement(By.id("signup")).click();
		return new SignupPage(driver, contextPath);
	}

	public LoginPage chooseToLogin()
	{
		driver.findElement(By.id("login")).click();
		return new LoginPage(driver, contextPath);
	}

	public String[] fetchSuccessMessages()
	{
		List<String> errorMessages = new ArrayList<String>();
		for (WebElement element : driver.findElements(By.xpath("//ul[@id='messages']/li[@class='infoStyle']")))
		{
			errorMessages.add(element.getText());
		}
		return errorMessages.toArray(new String[0]);
	}
}
