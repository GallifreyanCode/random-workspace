package be.gallifreyan.ws.server.test;

import javax.inject.Inject;
import javax.xml.transform.Source;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;

import be.gallifreyan.ws.server.AbstractServerTesting;

public class UserServiceEndpointITest extends AbstractServerTesting {
	@Inject
	private ApplicationContext applicationContext;
	private MockWebServiceClient mockClient;

	@Before
	public void setUp() {
		Assert.assertNotNull(applicationContext);
		mockClient = MockWebServiceClient.createClient(applicationContext);
		Assert.assertNotNull(mockClient);
	}

	@Test
	public void testCreate() {
		Source requestPayload = new StringSource(
				"<ns2:UserProfileCreateRequest xmlns:ns2='http://gallifreyan.be/usermanagement/schemas'>" +
						"<ns2:UserProfile>" +
							"<ns2:firstName>FirstName</ns2:firstName>" +
							"<ns2:lastName>LastName</ns2:lastName>" +
							"<ns2:age>30</ns2:age>" +
				"</ns2:UserProfile></ns2:UserProfileCreateRequest>");
		Source responsePayload =  new StringSource(
				"<ns2:UserProfileCreateResponse xmlns:ns2='http://gallifreyan.be/usermanagement/schemas'>" +
						"<ns2:message>user created successfully</ns2:message>" +
				"</ns2:UserProfileCreateResponse>");		
		testRequest(requestPayload, responsePayload);
	}
	
	private void testRequest(Source requestPayload, Source responsePayload) { 
		mockClient.sendRequest(RequestCreators.withPayload(requestPayload))
			.andExpect(ResponseMatchers.payload(responsePayload));
	}
	
	@After
	public void tearDown() {
		mockClient = null;
		Assert.assertNull(mockClient);
	}
}
