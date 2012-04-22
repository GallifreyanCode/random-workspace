package be.gallifreyan.javaee.view.album;

import be.gallifreyan.javaee.view.user.HomePage;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;

public class CreateAlbumPage
{

	private WebDriver driver;
	private URI contextPath;

	public CreateAlbumPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("wrapper")));
		if (!driver.getTitle().equals("Create an album"))
		{
			throw new IllegalStateException("This is not the Create Album page.");
		}
	}

	public HomePage createAlbumAs(String albumName, String albumDescription)
	{
		driver.findElement(By.id("CreateAlbum:albumName")).clear();
		driver.findElement(By.id("CreateAlbum:albumName")).sendKeys(albumName);
		driver.findElement(By.id("CreateAlbum:albumDescription")).clear();
		driver.findElement(By.id("CreateAlbum:albumDescription")).sendKeys(albumDescription);
		driver.findElement(By.id("CreateAlbum:createAlbumButton")).click();
		return new HomePage(driver, contextPath);
	}

	public CreateAlbumPage createAlbumAsExpectingError(String albumName, String albumDescription)
	{
		driver.findElement(By.id("CreateAlbum:albumName")).clear();
		driver.findElement(By.id("CreateAlbum:albumName")).sendKeys(albumName);
		driver.findElement(By.id("CreateAlbum:albumDescription")).clear();
		driver.findElement(By.id("CreateAlbum:albumDescription")).sendKeys(albumDescription);
		driver.findElement(By.id("CreateAlbum:createAlbumButton")).click();
		return new CreateAlbumPage(driver, contextPath);
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
