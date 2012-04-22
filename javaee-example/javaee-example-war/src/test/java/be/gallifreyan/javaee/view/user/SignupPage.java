package be.gallifreyan.javaee.view.user;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;

public class SignupPage
{

	private WebDriver driver;
	private URI contextPath;

	public SignupPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("publicWrapper")));
		if (!driver.getTitle().equals("Sign up with Galleria"))
		{
			throw new IllegalStateException("This page is not the sign up page.");
		}
	}

	public IndexPage signupAs(String userId, String password)
	{
		driver.findElement(By.id("signupForm:userId")).clear();
		driver.findElement(By.id("signupForm:userId")).sendKeys(userId);
		driver.findElement(By.id("signupForm:password")).clear();
		driver.findElement(By.id("signupForm:password")).sendKeys(password);
		driver.findElement(By.id("signupForm:confirmPassword")).clear();
		driver.findElement(By.id("signupForm:confirmPassword")).sendKeys(password);
		driver.findElement(By.id("signupForm:submit")).click();
		return new IndexPage(driver, contextPath);
	}

	public SignupPage signupAsExpectingError(String userId, String password, String confirmPassword)
	{
		driver.findElement(By.id("signupForm:userId")).clear();
		driver.findElement(By.id("signupForm:userId")).sendKeys(userId);
		driver.findElement(By.id("signupForm:password")).clear();
		driver.findElement(By.id("signupForm:password")).sendKeys(password);
		driver.findElement(By.id("signupForm:confirmPassword")).clear();
		driver.findElement(By.id("signupForm:confirmPassword")).sendKeys(confirmPassword);
		driver.findElement(By.id("signupForm:submit")).click();
		return new SignupPage(driver, contextPath);
	}

	public String[] fetchSignupErrorMessages()
	{
		List<String> errorMessages = new ArrayList<String>();
		for (WebElement element : driver.findElements(By.xpath("//ul[@id='messages']/li[@class='errorStyle']")))
		{
			errorMessages.add(element.getText());
		}
		return errorMessages.toArray(new String[0]);
	}
}
