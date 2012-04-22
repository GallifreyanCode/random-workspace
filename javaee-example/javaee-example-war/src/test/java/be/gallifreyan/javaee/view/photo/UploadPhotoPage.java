package be.gallifreyan.javaee.view.photo;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;
import be.gallifreyan.javaee.view.album.ViewAlbumPage;

public class UploadPhotoPage
{

	private WebDriver driver;
	private URI contextPath;

	public UploadPhotoPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("wrapper")));
		if (!driver.getTitle().equals("Upload a photo"))
		{
			throw new IllegalStateException("This is not the Upload Photo page.");
		}
	}

	public ViewAlbumPage uploadAs(String photoTitle, String photoDescription, String filePath)
	{
		driver.findElement(By.id("multipartForm:photoTitle")).clear();
		driver.findElement(By.id("multipartForm:photoTitle")).sendKeys(photoTitle);
		driver.findElement(By.id("multipartForm:photoDescription")).clear();
		driver.findElement(By.id("multipartForm:photoDescription")).sendKeys(photoDescription);
		driver.findElement(By.id("multipartForm:photoUploader")).sendKeys(filePath);
		driver.findElement(By.id("multipartForm:uploadButton")).click();
		return new ViewAlbumPage(driver, contextPath);
	}

	public UploadPhotoPage uploadAsExpectingError(String photoTitle, String photoDescription, String filePath)
	{
		driver.findElement(By.id("multipartForm:photoTitle")).clear();
		driver.findElement(By.id("multipartForm:photoTitle")).sendKeys(photoTitle);
		driver.findElement(By.id("multipartForm:photoDescription")).clear();
		driver.findElement(By.id("multipartForm:photoDescription")).sendKeys(photoDescription);
		driver.findElement(By.id("multipartForm:photoUploader")).sendKeys(filePath);
		driver.findElement(By.id("multipartForm:uploadButton")).click();
		return new UploadPhotoPage(driver, contextPath);
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
