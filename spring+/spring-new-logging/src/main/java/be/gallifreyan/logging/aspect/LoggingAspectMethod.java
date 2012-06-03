package be.gallifreyan.logging.aspect;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import be.gallifreyan.logging.LogLevel;
import be.gallifreyan.logging.annotation.LoggableMethod;

import javax.annotation.Resource;

@Aspect
public class LoggingAspectMethod extends LoggingAspect {

	//@Resource
	private Logger logger = LoggerFactory.getLogger(LoggingAspectMethod.class);

    @Before(value = "@annotation(loggable)", argNames = "joinPoint, loggable")
    public void before(JoinPoint joinPoint, LoggableMethod loggable) {

        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String name = joinPoint.getSignature().getName();

        if (ArrayUtils.isEmpty(joinPoint.getArgs())) {
        	logger.error("djfxsjddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
//            logger.log(loggable.value(), clazz, null, BEFORE_STRING, name,
//                    constructArgumentsString(clazz, joinPoint.getArgs()));
        } else {
        	logger.error("djfxsjddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
//            logger.log(loggable.value(), clazz, null, BEFORE_WITH_PARAMS_STRING, name,
//                    constructArgumentsString(clazz, joinPoint.getArgs()));
        }
    }

    @AfterThrowing(value = "@annotation(be.gallifreyan.logging.annotation.LoggableMethod)",
            throwing = "throwable", argNames = "joinPoint, throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {

        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String name = joinPoint.getSignature().getName();
//        logger.log(LogLevel.ERROR, clazz, throwable, AFTER_THROWING, name,
//                throwable.getMessage(), constructArgumentsString(clazz,
//                joinPoint.getArgs()));
        logger.error("djfxsjddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
    }

    @AfterReturning(value = "@annotation(loggable)", returning = "returnValue",
            argNames = "joinPoint, loggable, returnValue")
    public void afterReturning(JoinPoint joinPoint, LoggableMethod loggable,
                               Object returnValue) {

        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String name = joinPoint.getSignature().getName();

        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) joinPoint
                    .getSignature();
            Class<?> returnType = signature.getReturnType();
            if (returnType.getName().compareTo("void") == 0) {
//                logger.log(loggable.value(), clazz, null, AFTER_RETURNING_VOID,
//                        name, constructArgumentsString(clazz, returnValue));
            	logger.error("djfxsjddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
                return;
            }
        }

//        logger.log(loggable.value(), clazz, null, AFTER_RETURNING, name,
//                constructArgumentsString(clazz, returnValue));
        logger.error("djfxsjddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
    }
}
