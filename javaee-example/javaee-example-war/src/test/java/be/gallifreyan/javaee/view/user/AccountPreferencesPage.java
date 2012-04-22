package be.gallifreyan.javaee.view.user;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;

public class AccountPreferencesPage
{

	private WebDriver driver;
	private URI contextPath;

	public AccountPreferencesPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("wrapper")));
		if (!driver.getTitle().equals("Account Preferences"))
		{
			throw new IllegalStateException("This is not the Account Preferences page.");
		}
	}

	public HomePage modifyPasswordAs(String currentPassword, String newPassword, String confirmNewPassword)
	{
		driver.findElement(By.id("modifyPasswordForm:oldPassword")).clear();
		driver.findElement(By.id("modifyPasswordForm:oldPassword")).sendKeys(currentPassword);
		driver.findElement(By.id("modifyPasswordForm:newPassword")).clear();
		driver.findElement(By.id("modifyPasswordForm:newPassword")).sendKeys(newPassword);
		driver.findElement(By.id("modifyPasswordForm:confirmNewPassword")).clear();
		driver.findElement(By.id("modifyPasswordForm:confirmNewPassword")).sendKeys(confirmNewPassword);
		driver.findElement(By.id("modifyPasswordForm:modifyPassword")).click();
		return new HomePage(driver, contextPath);
	}

	public AccountPreferencesPage modifyPasswordAsExpectingError(String currentPassword, String newPassword,
			String confirmNewPassword)
	{
		driver.findElement(By.id("modifyPasswordForm:oldPassword")).clear();
		driver.findElement(By.id("modifyPasswordForm:oldPassword")).sendKeys(currentPassword);
		driver.findElement(By.id("modifyPasswordForm:newPassword")).clear();
		driver.findElement(By.id("modifyPasswordForm:newPassword")).sendKeys(newPassword);
		driver.findElement(By.id("modifyPasswordForm:confirmNewPassword")).clear();
		driver.findElement(By.id("modifyPasswordForm:confirmNewPassword")).sendKeys(confirmNewPassword);
		driver.findElement(By.id("modifyPasswordForm:modifyPassword")).click();
		return new AccountPreferencesPage(driver, contextPath);
	}

	public IndexPage deleteAcccount()
	{
		driver.findElement(By.id("deleteAccountForm:deleteAccount")).click();
		return new IndexPage(driver, contextPath);
	}

	public String[] fetchErrorMessages()
	{
		List<String> errorMessages = new ArrayList<String>();
		for (WebElement element : driver.findElements(By.xpath("//ul[@id='messages']/li[@class='errorStyle']")))
		{
			errorMessages.add(element.getText());
		}
		return errorMessages.toArray(new String[0]);
	}
}
