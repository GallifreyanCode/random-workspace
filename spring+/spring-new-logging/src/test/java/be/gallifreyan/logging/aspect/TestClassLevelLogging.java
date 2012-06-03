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
import be.gallifreyan.logging.bean.ClassLevelLoggingBean;
import be.gallifreyan.logging.logger.MockLogger;

import java.math.BigDecimal;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//		"/applicationContext/applicationContext-aspect.xml",
//		"/applicationContext/applicationContext-logger.xml",
//		"/applicationContext/applicationContext.xml" })
public class TestClassLevelLogging {

//	@Autowired
//	private MockLogger logger;
//
//	@Autowired
//    @Qualifier(value = "classLevelLoggingBean")
//	public ClassLevelLoggingBean classLevelLoggingBean;
//
//	@Before
//	public void before() {
//		logger.setLogLevel(ClassLevelLoggingBean.class, LogLevel.TRACE);
//		logger.resetLoggers();
//	}
//
//    @Test
//    public void testSimpleBeanSubclass_SetDateProperty() throws Exception {
//        classLevelLoggingBean.setDateProperty(
//                DateUtils.parseDate("01/01/2010", new String[] {"dd/MM/yyyy"}));
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < setDateProperty > with params Fri Jan 01 00:00:00 CET 2010 ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < setDateProperty > ]");
//    }
//
//    @Test
//    public void testSimpleBeanSubclass_SetDecimalProperty() {
//        classLevelLoggingBean.setDecimalProperty(new BigDecimal("0.25"));
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < setDecimalProperty > with params 0.25 ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < setDecimalProperty > ]");
//    }
//
//    @Test
//    public void testSimpleBeanSubclass_SetIntegerProperty() {
//        classLevelLoggingBean.setIntegerProperty(100);
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < setIntegerProperty > with params 100 ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < setIntegerProperty > ]");
//    }
//
//    @Test
//    public void testSimpleBeanSubclass_SetStringProperty() {
//        classLevelLoggingBean.setStringProperty("stringProperty");
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < setStringProperty > with params stringProperty ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < setStringProperty > ]");
//    }
//
//    @Test
//    public void testSimpleBeanSubclass_GetDateProperty() {
//        classLevelLoggingBean.getDateProperty();
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < getDateProperty > ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < getDateProperty > returning Fri Jan 01 00:00:00 CET 2010 ]");
//    }
//
//    @Test
//    public void testSimpleBeanSubclass_GetDecimalProperty() {
//        classLevelLoggingBean.getDecimalProperty();
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < getDecimalProperty > ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < getDecimalProperty > returning 0.25 ]");
//    }
//
//    @Test
//    public void testSimpleBeanSubclass_GetIntegerProperty() {
//        classLevelLoggingBean.getIntegerProperty();
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < getIntegerProperty > ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < getIntegerProperty > returning 100 ]");
//    }
//
//@Test
//    public void testSimpleBeanSubclass_GetStringProperty() {
//        classLevelLoggingBean.getStringProperty();
//
//        Assert.assertEquals(2, logger.getMessages(ClassLevelLoggingBean.class).size());
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(0),
//                LogLevel.TRACE,
//                "[ entering < getStringProperty > ]");
//        assertEquals(logger.getMessages(ClassLevelLoggingBean.class).get(1),
//                LogLevel.TRACE,
//                "[ leaving < getStringProperty > returning stringProperty ]");
//    }
//
//	private void assertEquals(MockLogger.LogMessage logMessage, LogLevel logLevel, String message) {
//		Assert.assertEquals(logLevel, logMessage.getLogLevel());
//		//Assert.assertEquals(message, logMessage.getMessage());		
//	}
}
