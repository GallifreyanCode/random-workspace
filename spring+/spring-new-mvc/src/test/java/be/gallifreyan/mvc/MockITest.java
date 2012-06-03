package be.gallifreyan.mvc;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import be.gallifreyan.config.ContextConfig;
import be.gallifreyan.config.PersistenceJPAConfig;
import be.gallifreyan.config.profile.MemoryDataConfig;
import be.gallifreyan.config.profile.StandaloneDataConfig;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {	ContextConfig.class,
//									PersistenceJPAConfig.class,
//									StandaloneDataConfig.class,
//									MemoryDataConfig.class
//								})
//@ActiveProfiles("test")
public class MockITest {
//    @Inject
//    private ApplicationContext applicationContext;
//    private MockHttpServletRequest request;
//    private MockHttpServletResponse response;
//    private HandlerAdapter handlerAdapter;
//    private HomeController controller;
//    
//
//    @Before
//    public void setUp() {
//       request = new MockHttpServletRequest();
//       response = new MockHttpServletResponse();
//       handlerAdapter = applicationContext.getBean(HandlerAdapter.class);
//       // I could get the controller from the context here
//       controller = new HomeController();
//    }
//    
//    @Test
//    public void testGet() throws Exception {
//       request.setMethod("GET");
//       final ModelAndView mav = handlerAdapter.handle(request, response, controller);
//       System.out.println(mav.getViewName());
////       assertViewName(mav, null);
////       assertAndReturnModelAttributeOfType(mav, "myForm", MyForm.class);
//    }
//
//    @Test
//    public void testPost() throws Exception {
//       request.setMethod("POST");
//       request.addParameter("firstName", "  Anthony  ");
//       final ModelAndView mav = handlerAdapter.handle(request, response, controller);
//       System.out.println(mav.getViewName());
////       final MyForm myForm = assertAndReturnModelAttributeOfType(mav, "myForm", MyForm.class);
////       assertEquals("Anthony", myForm.getFirstName());
//
//       /* if myForm is not valid */
////       assertViewName(mav, null);
////       final BindingResult errors = assertAndReturnModelAttributeOfType(mav,
////               "org.springframework.validation.BindingResult.myForm",
////               BindingResult.class);
////       assertTrue(errors.hasErrors());
//    }
//    
////	@Test
////	public void testGetForm() {
////		MockHttpServletRequest request = new MockHttpServletRequest();
////		HomeController controller = new HomeController();
////
////		request.setMethod("GET");
////		ModelAndView mav = null;
////		try {
////			 mav = controller.handleRequest(request, null);
////		} catch (Exception e) {
////			fail();
////		}
////		assertEquals("/book/searchForm.jsp", mav.getViewName());
////	}
}
