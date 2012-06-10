package be.gallifreyan.logging.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

import be.gallifreyan.logging.LogLevel;
import be.gallifreyan.logging.LoggerCreator;
import be.gallifreyan.logging.annotation.LoggableMethod;
import be.gallifreyan.logging.inject.Log;

@Aspect
public class LoggableAspect {
	private LogLevel defaultLogLevel = LogLevel.INFO;

	@Log
	Logger logger;

	@Pointcut("execution(* (@be.gallifreyan.logging.annotation.LoggableClass *).*(..))")
	public void loggingPointCut() {
	}
	
	/**
	 * Logs when method is entered within classes annotated with @LoggableClass.
	 * 
	 * @param joinPoint
	 */
	@Before("loggingPointCut()")
	public void before(JoinPoint joinPoint) {
		logger.debug("@Before");
		log(joinPoint, "Entered");
	}

	/**
	 * Logs when method is left within classes annotated with @LoggableClass.
	 * 
	 * @param joinPoint
	 * @param returnValue
	 * @throws Throwable
	 */
	@AfterReturning(value = "loggingPointCut()", returning = "returnValue", argNames = "joinPoint, returnValue")
	public void afterReturning(JoinPoint joinPoint, Object returnValue)
			throws Throwable {
		logger.debug("@AfterReturning')");
		log(joinPoint, "Exit...");
	}
	
	@AfterThrowing(value = "loggingPointCut()", throwing = "throwable", argNames = "joinPoint, throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Throwable {
		logger.debug("@AfterThrowing')");
        log(joinPoint, throwable);
    }

	/**
	 * Logs the message on the default level, it starts the creation process of the logger and the
	 * message.
	 * 
	 * @param joinPoint
	 * @param type
	 */
	private void log(JoinPoint joinPoint, String type) {
		Class<? extends Object> clazz = joinPoint.getTarget().getClass();
		String msg = "---=[" + type + "]=--- " + parseData(joinPoint);
		LoggerCreator.log(LoggerCreator.createLogger(clazz), msg,
				defaultLogLevel);
	}
	
	/**
	 * Logs the message on error level, it starts the creation process of the logger and the
	 * message.
	 * @param joinPoint
	 * @param throwable
	 */
	private void log(JoinPoint joinPoint, Throwable throwable) {
		Class<? extends Object> clazz = joinPoint.getTarget().getClass();
		String msg = "---=[" + "FAIL!!!" + "]=--- " + parseData(joinPoint);
		LoggerCreator.log(LoggerCreator.createLogger(clazz), msg,
				LogLevel.ERROR, throwable);
	}

	@Around("@annotation( loggableMethodAnnotation )")
	public Object processSystemRequest(final ProceedingJoinPoint pjp,
			LoggableMethod loggableMethodAnnotation) throws Throwable {
		try {
			Object retVal = pjp.proceed();
			System.out
					.println("---------------------------------------------------------------------------");
			logger.debug(parseData(pjp));
			System.out
					.println("---------------------------------------------------------------------------");
			return retVal;
		} catch (Throwable t) {
			System.out.println("error occured");
			throw t;
		}
	}

	/**
	 * Parses the joinpoint into a readable message related to the method.
	 * 
	 * @param pjp
	 * @return
	 */
	private String parseData(JoinPoint pjp) {
		String methodModifier = parseMethodModifier(pjp);
		String methodName = parseMethodName(pjp);
		String returnType = parseMethodReturnType(pjp);
		//String declaringClass = parseDeclaringClass(pjp);

		List<String> parameterNames = new ArrayList<String>();
		for (Class<?> c : parseMethodScope(pjp)) {
			parameterNames.add(c.getSimpleName());
		}

		String fullMethodString = methodModifier + " " + returnType + " "
				+ methodName + "(";

		boolean start = true;
		for (String s : parameterNames) {
			if (!start) {
				fullMethodString = fullMethodString + ", ";
			}
			fullMethodString = fullMethodString + s;
		}
		return fullMethodString + ")";
	}

	private String parseMethodName(JoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		return method.getName();
	}

	private String parseMethodReturnType(JoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		return method.getReturnType().toString();
	}

	private String parseMethodModifier(JoinPoint pjp) {
		String parsedModifier = "";
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		int modifier = method.getModifiers();
		switch (modifier) {
		case Modifier.PUBLIC:
			parsedModifier = "public";
			break;
		case Modifier.PRIVATE:
			parsedModifier = "private";
			break;
		}
		return parsedModifier;
	}

	private Class<?>[] parseMethodScope(JoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		return method.getParameterTypes();
	}

//	private String parseDeclaringClass(JoinPoint pjp) {
//		MethodSignature signature = (MethodSignature) pjp.getSignature();
//		Method method = signature.getMethod();
//		return method.getDeclaringClass().getSimpleName();
//	}

	public void setDefaultLogLevel(LogLevel defaultLogLevel) {
		if (defaultLogLevel != null) {
			this.defaultLogLevel = defaultLogLevel;
		}
	}
}
