package be.gallifreyan.mvc.itest.scenario;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import cucumber.annotation.After;

@Component
public class WebDriverFacade {

	private static Constructor<WebDriver> driverConstructor = getDriverConstructor();

	@SuppressWarnings("unchecked")
	private static Constructor<WebDriver> getDriverConstructor() {
		String driverName = System.getProperty("webdriver.impl", System.getProperty("webdriver"));
//		System.setProperty("webdriver.chrome.driver", "D:\\Documents and Settings\\EXF133\\Desktop\\spring+\\spring-new-mvc\\src\\test\\resources\\chromedriver\\chromedriver.exe");
//		String driverName = "org.openqa.selenium.chrome.ChromeDriver";
		try {
			System.out.println("CONSTRUCT BROWSER");
			return (Constructor<WebDriver>) Thread.currentThread().getContextClassLoader().loadClass(driverName).getConstructor();
		} catch (Throwable problem) {
			problem.printStackTrace();
			throw new RuntimeException("Couldn't load " + driverName, problem);
		}
	}

	private static WebDriver browser;

	public WebDriver getWebDriver() throws InvocationTargetException, IllegalAccessException, InstantiationException {
		if(browser == null) {
			browser = driverConstructor.newInstance();
		}
		return browser;
	}

	@After
	public void closeBrowser() throws IllegalAccessException, InvocationTargetException, InstantiationException {
		System.out.println("closeBrowser()");
		if(browser.toString().contains("Chrome")){
			browser.quit();
		} else {
			if (browser != null) {
				browser.close();
			}
		}		
	}
}