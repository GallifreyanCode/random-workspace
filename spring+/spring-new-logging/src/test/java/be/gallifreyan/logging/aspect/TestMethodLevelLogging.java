package be.gallifreyan.logging.aspect;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.gallifreyan.logging.LogLevel;
import be.gallifreyan.logging.bean.MethodLevelLoggingBean;
import be.gallifreyan.logging.logger.MockLogger;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//		"/applicationContext/applicationContext-aspect.xml",
//		"/applicationContext/applicationContext-logger.xml",
//		"/applicationContext/applicationContext.xml" })
public class TestMethodLevelLogging {

//	@Autowired
//	private MockLogger logger;
//	
//	@Autowired
//	@Qualifier(value = "methodLevelLoggingBean")
//	public MethodLevelLoggingBean methodLevelLoggingBean;
//
//	@Before
//	public void before() {
//		logger.setLogLevel(MethodLevelLoggingBean.class, LogLevel.TRACE);
//		logger.resetLoggers();
//	}
//	
//	@Test
//	public void testSimpleBean_SetDateProperty() throws Exception {
//        methodLevelLoggingBean.setDateProperty(
//				DateUtils.parseDate("01/01/2010", new String[] {"dd/MM/yyyy"}));
//		
//		Assert.assertEquals(2, logger.getMessages(MethodLevelLoggingBean.class).size());
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(0),
//				LogLevel.TRACE,
//				"[ entering < setDateProperty > with params Fri Jan 01 00:00:00 CET 2010 ]");
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(1),
//				LogLevel.TRACE,
//				"[ leaving < setDateProperty > ]");
//	}
//	
//	@Test
//	public void testSimpleBean_SetIntegerProperty() {
//        methodLevelLoggingBean.setIntegerProperty(100);
//		
//		Assert.assertEquals(2, logger.getMessages(MethodLevelLoggingBean.class).size());
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(0),
//				LogLevel.TRACE,
//				"[ entering < setIntegerProperty > with params 100 ]");
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(1),
//				LogLevel.TRACE,
//				"[ leaving < setIntegerProperty > ]");
//	}
//	
//	@Test
//	public void testSimpleBean_SetStringProperty() {
//        methodLevelLoggingBean.setStringProperty("stringProperty");
//		
//		Assert.assertEquals(2, logger.getMessages(MethodLevelLoggingBean.class).size());
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(0),
//				LogLevel.TRACE,
//				"[ entering < setStringProperty > with params stringProperty ]");
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(1),
//				LogLevel.TRACE,
//				"[ leaving < setStringProperty > ]");
//	}
//	
//	@Test
//	public void testSimpleBean_GetDateProperty() {
//        methodLevelLoggingBean.getDateProperty();
//		
//		Assert.assertEquals(2, logger.getMessages(MethodLevelLoggingBean.class).size());
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(0),
//				LogLevel.TRACE,
//				"[ entering < getDateProperty > ]");
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(1),
//				LogLevel.TRACE,
//				"[ leaving < getDateProperty > returning Fri Jan 01 00:00:00 CET 2010 ]");
//	}
//	
//	@Test
//	public void testSimpleBean_GetIntegerProperty() {
//        methodLevelLoggingBean.getIntegerProperty();
//		
//		Assert.assertEquals(2, logger.getMessages(MethodLevelLoggingBean.class).size());
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(0),
//				LogLevel.TRACE,
//				"[ entering < getIntegerProperty > ]");
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(1),
//				LogLevel.TRACE,
//				"[ leaving < getIntegerProperty > returning 100 ]");
//	}
//	
//	@Test
//	public void testSimpleBean_GetStringProperty() {
//        methodLevelLoggingBean.getStringProperty();
//
//		Assert.assertEquals(2, logger.getMessages(MethodLevelLoggingBean.class).size());
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(0),
//				LogLevel.TRACE,
//				"[ entering < getStringProperty > ]");
//		assertEquals(logger.getMessages(MethodLevelLoggingBean.class).get(1),
//				LogLevel.TRACE,
//				"[ leaving < getStringProperty > returning stringProperty ]");
//	}
//
//	private void assertEquals(MockLogger.LogMessage logMessage, LogLevel logLevel, String message) {
//		Assert.assertEquals(logLevel, logMessage.getLogLevel());
//		//Assert.assertEquals(message, logMessage.getMessage());		
//	}
}
