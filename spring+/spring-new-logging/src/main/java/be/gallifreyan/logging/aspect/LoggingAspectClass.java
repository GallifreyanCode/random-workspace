package be.gallifreyan.logging.aspect;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
public class LoggingAspectClass extends LoggingAspect {
	private static Logger logger = LoggerFactory
			.getLogger(LoggingAspectClass.class);

    @Pointcut("execution(* (@be.gallifreyan.logging.annotation.LoggableClass *).*(..))")
    public void loggingPointCut(){}

    @Before("loggingPointCut()")
    public void before(JoinPoint joinPoint) {
    	logger.debug(joinPoint.getSignature().getName() + " - BEFORE");
       System.out.println(joinPoint.getSignature().getName() + " - BEFORE");

        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String name = joinPoint.getSignature().getName();

        if (ArrayUtils.isEmpty(joinPoint.getArgs())) {
        	logger.error("sdfjfjsdfjsjdfjsdjfjsdjfjsdfjsjdfjsjfjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
//            logger.log(LOG_LEVEL, clazz, null, BEFORE_STRING, name,
//                    constructArgumentsString(clazz, joinPoint.getArgs()));
        } else {
        	logger.error("sdfjfjsdfjsjdfjsdjfjsdjfjsdfjsjdfjsjfjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
//            logger.log(LOG_LEVEL, clazz, null, BEFORE_WITH_PARAMS_STRING, name,
//                    constructArgumentsString(clazz, joinPoint.getArgs()));
        }
    }

    @AfterThrowing(value = "loggingPointCut()", throwing = "throwable", argNames = "joinPoint, throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Throwable {
        System.out.println(joinPoint.getSignature().getName() + " - AFTER THROWING");
        logger.debug(joinPoint.getSignature().getName() + " - AFTER THROWING");
        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String name = joinPoint.getSignature().getName();
//        logger.log(LogLevel.ERROR, clazz, throwable, AFTER_THROWING, name,
//                throwable.getMessage(), constructArgumentsString(clazz,
//                joinPoint.getArgs()));
        logger.error("sdfjfjsdfjsjdfjsdjfjsdjfjsdfjsjdfjsjfjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
    }

    @AfterReturning(value = "loggingPointCut()", returning = "returnValue",
            argNames = "joinPoint, returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) throws Throwable {
        System.out.println(joinPoint.getSignature().getName() + " - AFTER RETURNING" + " + Return Value " + returnValue);

        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String name = joinPoint.getSignature().getName();

        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) joinPoint
                    .getSignature();
            Class<?> returnType = signature.getReturnType();
            if (returnType.getName().compareTo("void") == 0) {
//                logger.log(LOG_LEVEL, clazz, null, AFTER_RETURNING_VOID,
//                        name, constructArgumentsString(clazz, returnValue));
            	logger.error("sdfjfjsdfjsjdfjsdjfjsdjfjsdfjsjdfjsjfjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
                return;
            }
        }

//        logger.log(LOG_LEVEL, clazz, null, AFTER_RETURNING, name,
//                constructArgumentsString(clazz, returnValue));

        logger.error("sdfjfjsdfjsjdfjsdjfjsdjfjsdfjsjdfjsjfjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
    }
}
