package be.gallifreyan.itest.logging;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import be.gallifreyan.logging.annotation.LoggableMethod;
import be.gallifreyan.logging.inject.Log;

@Component
public class LoggingMethodTestBean {
	@Log
	Logger testLogger;
	
	@LoggableMethod(value = "test")
	public void getLogger() throws InterruptedException {
		Thread.sleep(100);
	}
	
	public Logger getTestLogger() {
		return testLogger;
	}
}
