package be.gallifreyan.logback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingDebugTest {
	private static final Logger logger = LoggerFactory
			.getLogger(LoggingDebugTest.class);

	@Test
	public void bar() {
		String message = "Hello Debug World";
		logger.info("Hello info message.");
		logger.debug("Hello debug message, {}.", message);
	}
}
