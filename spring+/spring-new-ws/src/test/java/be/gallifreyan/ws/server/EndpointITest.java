package be.gallifreyan.ws.server;

import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(SpringJUnit4ClassRunner.class)
public class EndpointITest extends AbstractServerTesting {
	@Inject
	private ApplicationContext applicationContext;
	private MockWebServiceClient mockClient;

	@Before
	public void createClient() {
		Assert.assertNotNull(applicationContext);
		mockClient = MockWebServiceClient.createClient(applicationContext);
		Assert.assertNotNull(mockClient);
	}

//	@Test
//	public void customerEndpoint() throws Exception {
//		Source requestPayload = new StringSource(
//				"<ns2:placeOrderRequest xmlns:ns2='http://www.liverestaurant.com/OrderService/schema'>" +
//						"<ns2:order>" +
//							"<ns2:customer>" +
//									"<ns2:dateSubmitted>01/01/01</ns2:dateSubmitted>" +
//									"<ns2:orderData>01/01/01</ns2:orderData>" +
//									"<ns2:items>" +
//											"<ns2:foodItem>" +
//											"</ns2:foodItem>" +
//									"</ns2:items>" +
//							"</ns2:customer>" +
//						"</ns2:order>"
//						+ "</ns2:placeOrderRequest>");
//		Source responsePayload = new StringSource(
//				"<ns2:placeOrderResponse xmlns:ns2='http://www.liverestaurant.com/OrderService/schema'>"
//						+ "<ns2:refNumber>10</ns2:refNumber>"
//						+ "</ns2:placeOrderResponse>");
//
//		mockClient.sendRequest(withPayload(requestPayload)).andExpect(
//				payload(responsePayload));
//	}
}
