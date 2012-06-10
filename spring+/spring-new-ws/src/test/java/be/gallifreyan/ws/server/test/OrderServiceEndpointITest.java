package be.gallifreyan.ws.server.test;

import javax.inject.Inject;
import javax.xml.transform.Source;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;

import be.gallifreyan.ws.server.AbstractServerTesting;

public class OrderServiceEndpointITest extends AbstractServerTesting {
	@Inject
	private ApplicationContext applicationContext;
	private MockWebServiceClient mockClient;

	@Before
	public void createClient() {
		Assert.assertNotNull(applicationContext);
		mockClient = MockWebServiceClient.createClient(applicationContext);
		Assert.assertNotNull(mockClient);
	}

	@Test
	public void customerEndpoint() throws Exception {
		Source requestPayload = new StringSource(
				"<ns2:placeOrderRequest xmlns:ns2='http://www.liverestaurant.com/OrderService/schema'>" +
						"<ns2:order>" +
							"<ns2:customer>" +
									"<ns2:dateSubmitted>01/01/01</ns2:dateSubmitted>" +
									"<ns2:orderData>01/01/01</ns2:orderData>" +
									"<ns2:items>" +
											"<ns2:foodItem>" +
											"</ns2:foodItem>" +
									"</ns2:items>" +
							"</ns2:customer>" +
						"</ns2:order>"
						+ "</ns2:placeOrderRequest>");
		Source responsePayload = new StringSource(
							"<ns2:placeOrderResponse xmlns:ns2='http://www.liverestaurant.com/OrderService/schema'>" +
								"<ns2:refNumber>10</ns2:refNumber>" +
							"</ns2:placeOrderResponse>");
		mockClient.sendRequest(RequestCreators.withPayload(requestPayload)).andExpect(
				ResponseMatchers.payload(responsePayload));
	}
}
