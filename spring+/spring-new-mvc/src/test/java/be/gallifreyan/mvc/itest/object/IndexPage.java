package be.gallifreyan.mvc.itest.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IndexPage extends PageObject {
	
	@FindBy(xpath="//p[1]")
	private WebElement pageMessage;
	
	public boolean hasPageMessage(String expectedMessage) {
		WebElement idfield = this.webDriver.findElement(By.xpath("//p[1]"));
		System.out.println(idfield.getText());
		return expectedMessage.equals(pageMessage.getText());
	}
}