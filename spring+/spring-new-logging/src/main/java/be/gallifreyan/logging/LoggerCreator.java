package be.gallifreyan.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerCreator {

	public static Logger createLogger(Class<? extends Object> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	public static void log(Logger logger, String msg, LogLevel logLevel) {
		log(logger, msg, logLevel, null);
	}
	public static void log(Logger logger, String msg, LogLevel logLevel, Throwable throwable) {
		switch (logLevel) {
		case INFO:
			logger.info(msg);
			break;
		case DEBUG:
			logger.debug(msg);
			break;
		case ERROR:
			logger.error(msg, throwable);
			break;
		}
	}
}
