package be.gallifreyan.javaee.view.user;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;
import be.gallifreyan.javaee.view.album.CreateAlbumPage;
import be.gallifreyan.javaee.view.album.ViewAlbumPage;
import be.gallifreyan.javaee.view.photo.UploadPhotoPage;

public class HomePage
{

	private WebDriver driver;
	private URI contextPath;

	public HomePage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("wrapper")));
		if (!driver.getTitle().equals("Galleria"))
		{
			throw new IllegalStateException("This is not the home page.");
		}
	}

	public LoginPage logout()
	{
		driver.findElement(By.id("commonActions:logout")).click();
		return new LoginPage(driver, contextPath);
	}

	public AccountPreferencesPage goToAccountPreferences()
	{
		driver.findElement(By.id("commonActions:AccountPreferences")).click();
		return new AccountPreferencesPage(driver, contextPath);
	}

	public String[] fetchSuccessMessages()
	{
		List<String> messages = new ArrayList<String>();
		for (WebElement element : driver.findElements(By.xpath("//ul[@id='messages']/li[@class='infoStyle']")))
		{
			messages.add(element.getText());
		}
		return messages.toArray(new String[0]);
	}

	public CreateAlbumPage goToCreateAlbum()
	{
		driver.findElement(By.id("CreateAlbum")).click();
		return new CreateAlbumPage(driver, contextPath);
	}

	public ViewAlbumPage viewAlbumWith(String albumName, String albumDescription)
	{
		List<WebElement> photoWrappers = driver.findElements(By.xpath("//ul[@class='photoWrapper']"));
		for (WebElement element : photoWrappers)
		{
			WebElement name = element.findElement(By.className("name"));
			WebElement description = element.findElement(By.className("description"));
			if (name.getText().equals(albumName) && description.getText().equals(albumDescription))
			{
				element.findElement(By.tagName("a")).click();
				return new ViewAlbumPage(driver, contextPath);
			}
		}
		return null;
	}

	public UploadPhotoPage goToUploadPhoto()
	{
		driver.findElement(By.id("Upload")).click();
		return new UploadPhotoPage(driver, contextPath);
	}

}
