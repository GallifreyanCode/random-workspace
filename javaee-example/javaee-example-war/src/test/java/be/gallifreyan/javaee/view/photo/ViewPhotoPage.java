package be.gallifreyan.javaee.view.photo;

import java.net.URI;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import be.gallifreyan.javaee.view.PageUtilities;
import be.gallifreyan.javaee.view.album.ViewAlbumPage;

public class ViewPhotoPage
{

	private WebDriver driver;
	private URI contextPath;

	public ViewPhotoPage(WebDriver driver, URI contextPath)
	{
		this.driver = driver;
		this.contextPath = contextPath;
		Wait<WebDriver> wait = new WebDriverWait(driver, 15);
		wait.until(PageUtilities.visibilityOfElementLocated(By.id("wrapper")));
		if (!driver.getTitle().equals("View a photo"))
		{
			throw new IllegalStateException("This is not the View Photo page.");
		}
	}

	public EditPhotoPage goToEditPhoto()
	{
		driver.findElement(By.id("editPhotoLink")).click();
		return new EditPhotoPage(driver, contextPath);
	}

	public ViewAlbumPage deletePhoto()
	{
		driver.findElement(By.id("deletePhotoForm:deletePhotoButton")).click();
		return new ViewAlbumPage(driver, contextPath);
	}

	public ViewPhotoPage setAsCoverPhoto()
	{
		driver.findElement(By.id("setAlbumCoverForm:setAlbumCoverButton")).click();
		return new ViewPhotoPage(driver, contextPath);
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

}
