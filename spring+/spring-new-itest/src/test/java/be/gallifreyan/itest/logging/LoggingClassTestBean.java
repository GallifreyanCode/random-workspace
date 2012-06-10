package be.gallifreyan.itest.logging;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import be.gallifreyan.logging.annotation.LoggableClass;
import be.gallifreyan.logging.inject.Log;

@Component
@LoggableClass
public class LoggingClassTestBean {
	@Log
	Logger testLogger;
	
	public void getLogger() throws InterruptedException {
		Thread.sleep(100);
	}
	
	public void setTestLogger(Logger testLogger) {
		this.testLogger = testLogger;
	}
	
	public Logger getTestLogger() {
		return testLogger;
	}
	
	public void testThrowable() throws IOException {
		throw new IOException();
	}
}
