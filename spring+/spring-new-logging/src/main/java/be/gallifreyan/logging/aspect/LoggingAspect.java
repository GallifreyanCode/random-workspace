package be.gallifreyan.logging.aspect;


import be.gallifreyan.logging.LogLevel;

public class LoggingAspect {
    protected static String BEFORE_STRING = "[ entering < {0} > ]";

    protected static String BEFORE_WITH_PARAMS_STRING = "[ entering < {0} > with params {1} ]";

    protected static String AFTER_THROWING = "[ exception thrown < {0} > exception message {1} with params {2} ]";

    protected static String AFTER_RETURNING = "[ leaving < {0} > returning {1} ]";

    protected static String AFTER_RETURNING_VOID = "[ leaving < {0} > ]";

    protected static LogLevel LOG_LEVEL = LogLevel.TRACE;

    protected String constructArgumentsString(Class<?> clazz, Object... arguments) {
        StringBuffer buffer = new StringBuffer();
        for (Object object : arguments) {

            buffer.append(object);
        }
        return buffer.toString();
    }
}
