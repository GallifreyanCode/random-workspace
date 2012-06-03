package be.gallifreyan.logging;

public interface Logger {
    boolean isLogLevel(LogLevel logLevel, Class<?> clazz);

    void log(LogLevel logLevel, Class<?> clazz, Throwable throwable, String pattern, Object... arguments);
}
