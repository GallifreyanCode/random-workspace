package be.gallifreyan.ws.client.test;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.client.ResponseCreators;
import org.springframework.xml.transform.StringSource;

import be.gallifreyan.ws.client.AbstractClientTesting;

public class ProfileServiceClientITest extends AbstractClientTesting {
	private String request = "<ns2:UserProfileCreateRequest xmlns:ns2='http://gallifreyan.be/usermanagement/schemas'> 	<ns2:UserProfile>		<ns2:firstName>FirstName</ns2:firstName>		<ns2:lastName>LastName</ns2:lastName>		<ns2:age>30</ns2:age>	</ns2:UserProfile></ns2:UserProfileCreateRequest>";
	private String response = "<ns2:UserProfileCreateResponse xmlns:ns2='http://gallifreyan.be/usermanagement/schemas'>  <ns2:message>user created successfully</ns2:message>  </ns2:UserProfileCreateResponse>";

	@Inject
	@Named("profileServiceTemplate")
	private WebServiceTemplate webServiceTemplate;
	private MockWebServiceServer server;
	@Inject
	private ProfileServiceClient client;

	@Before
	public void setup() {
		server = MockWebServiceServer.createServer(webServiceTemplate);
	}

	@Test
	public void testInvokeProfileServiceAndGetASuccessResponse() {
		server.expect(RequestMatchers.payload(new StringSource(request)))
				.andRespond(
						ResponseCreators
								.withPayload(new StringSource(response)));
		Assert.assertEquals("user created successfully",
				client.invokeProfileServiceAndGetASuccessResponse());
		server.verify();
	}

	@Test
	public void shouldInvokeProfileServiceAndGetASuccessResponse()
			throws Exception {
		Resource schema = new FileSystemResource(
				"src/main/webapp/WEB-INF/xsd/UserManagement.xsd");
		server.expect(RequestMatchers.payload(new StringSource(request)))
				.andExpect(RequestMatchers.validPayload(schema))
				.andRespond(
						ResponseCreators
								.withPayload(new StringSource(response)));
		Assert.assertEquals("user created successfully",
				client.invokeProfileServiceAndGetASuccessResponse());
		server.verify();
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowRuntimeException() {
		server.expect(RequestMatchers.payload(new StringSource(request)))
				.andRespond(
						ResponseCreators.withException(new RuntimeException()));
		client.invokeProfileServiceAndGetASuccessResponse();
		server.verify();
	}

	@Test(expected = SoapFaultClientException.class)
	public void shouldThrowSoapFault() {
		server.expect(RequestMatchers.payload(new StringSource(request)))
				.andRespond(
						ResponseCreators.withServerOrReceiverFault(
								"Soap Fault Exception", Locale.US));
		client.invokeProfileServiceAndGetASuccessResponse();
		server.verify();
	}
}
