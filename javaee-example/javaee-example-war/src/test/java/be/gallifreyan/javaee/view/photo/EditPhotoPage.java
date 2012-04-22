package be.gallifreyan.javaee.view.photo;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;

public class EditPhotoPage
{

	private WebDriver driver;
	private URI contextPath;

	public EditPhotoPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("wrapper")));
		if (!driver.getTitle().equals("Edit a photo"))
		{
			throw new IllegalStateException("This is not the Edit Photo page.");
		}
	}

	public ViewPhotoPage editPhotoAs(String photoTitle, String photoDescription)
	{
		driver.findElement(By.id("editPhotoForm:photoTitle")).clear();
		driver.findElement(By.id("editPhotoForm:photoTitle")).sendKeys(photoTitle);
		driver.findElement(By.id("editPhotoForm:photoDescription")).clear();
		driver.findElement(By.id("editPhotoForm:photoDescription")).sendKeys(photoDescription);
		driver.findElement(By.id("editPhotoForm:editPhotoButton")).click();
		return new ViewPhotoPage(driver, contextPath);
	}

	public EditPhotoPage editPhotoAsExpectingError(String photoTitle, String photoDescription)
	{
		driver.findElement(By.id("editPhotoForm:photoTitle")).clear();
		driver.findElement(By.id("editPhotoForm:photoTitle")).sendKeys(photoTitle);
		driver.findElement(By.id("editPhotoForm:photoDescription")).clear();
		driver.findElement(By.id("editPhotoForm:photoDescription")).sendKeys(photoDescription);
		driver.findElement(By.id("editPhotoForm:editPhotoButton")).click();
		return new EditPhotoPage(driver, contextPath);
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
