package be.gallifreyan.javaee.view.album;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;

public class EditAlbumPage
{

	private WebDriver driver;
	private URI contextPath;

	public EditAlbumPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("wrapper")));
		if (!driver.getTitle().equals("Edit an album"))
		{
			throw new IllegalStateException("This is not the Edit Album page.");
		}
	}

	public ViewAlbumPage editAlbumAs(String albumName, String albumDescription)
	{
		driver.findElement(By.id("editAlbumForm:albumName")).clear();
		driver.findElement(By.id("editAlbumForm:albumName")).sendKeys(albumName);
		driver.findElement(By.id("editAlbumForm:albumDescription")).clear();
		driver.findElement(By.id("editAlbumForm:albumDescription")).sendKeys(albumDescription);
		driver.findElement(By.id("editAlbumForm:editAlbumButton")).click();
		return new ViewAlbumPage(driver, contextPath);
	}

	public EditAlbumPage editAlbumAsExpectingError(String albumName, String albumDescription)
	{
		driver.findElement(By.id("editAlbumForm:albumName")).clear();
		driver.findElement(By.id("editAlbumForm:albumName")).sendKeys(albumName);
		driver.findElement(By.id("editAlbumForm:albumDescription")).clear();
		driver.findElement(By.id("editAlbumForm:albumDescription")).sendKeys(albumDescription);
		driver.findElement(By.id("editAlbumForm:editAlbumButton")).click();
		return new EditAlbumPage(driver, contextPath);
	}

	public String[] fetchErrorMessages()
	{
		List<String> messages = new ArrayList<String>();
		for (WebElement element : driver.findElements(By.xpath("//ul[@id='messages']/li[@class='errorStyle']")))
		{
			messages.add(element.getText());
		}
		return messages.toArray(new String[0]);
	}

}
