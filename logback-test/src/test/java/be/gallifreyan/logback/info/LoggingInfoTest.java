package be.gallifreyan.logback.info;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingInfoTest {
	private static final Logger logger = LoggerFactory
			.getLogger(LoggingInfoTest.class);

	@Test
	public void bar() {
		String message = "Hello Debug World";
		logger.info("Hello info message.");
		logger.debug("Hello debug message, {}.", message);
	}
}
