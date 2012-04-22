package be.gallifreyan.javaee.view.user;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;

public class LoginPage
{

	private WebDriver driver;
	private URI contextPath;

	public LoginPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("publicWrapper")));
		if (!driver.getTitle().equals("Log in to Galleria"))
		{
			throw new IllegalStateException("This page is not the login page.");
		}
	}

	public HomePage loginAs(String userId, String password)
	{
		WebElement userIdField = driver.findElement(By.id("LoginForm:userid"));
		WebElement passwordField = driver.findElement(By.id("LoginForm:password"));
		WebElement submitButton = driver.findElement(By.id("LoginForm:submit"));
		userIdField.clear();
		userIdField.sendKeys(userId);
		passwordField.clear();
		passwordField.sendKeys(password);
		submitButton.click();
		return new HomePage(driver, contextPath);
	}

	public LoginPage loginAsExpectingError(String userId, String password)
	{
		WebElement userIdField = driver.findElement(By.id("LoginForm:userid"));
		WebElement passwordField = driver.findElement(By.id("LoginForm:password"));
		WebElement submitButton = driver.findElement(By.id("LoginForm:submit"));
		userIdField.clear();
		userIdField.sendKeys(userId);
		passwordField.clear();
		passwordField.sendKeys(password);
		submitButton.click();
		return new LoginPage(driver, contextPath);
	}

	public String[] getLoginErrorMessagesDisplayed()
	{
		List<String> errorMessages = new ArrayList<String>();
		for (WebElement element : driver.findElements(By.xpath("//ul[@id='messages']/li")))
		{
			errorMessages.add(element.getText());
		}
		return errorMessages.toArray(new String[0]);
	}
}
