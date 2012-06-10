package be.gallifreyan.ws.client.test;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import be.gallifreyan.ws.model.UserProfile;
import be.gallifreyan.ws.model.UserProfileCreateRequest;
import be.gallifreyan.ws.model.UserProfileCreateResponse;

@Component
public class ProfileServiceClient {
	@Inject
	@Named("profileServiceTemplate")
	WebServiceTemplate webServiceTemplate;

	public String invokeProfileServiceAndGetASuccessResponse() {
		UserProfileCreateRequest request = new UserProfileCreateRequest();
		UserProfile userProfile = new UserProfile();
		userProfile.setAge(BigInteger.valueOf(30));
		userProfile.setFirstName("FirstName");
		userProfile.setLastName("LastName");
		request.setUserProfile(userProfile);

		UserProfileCreateResponse response = (UserProfileCreateResponse) webServiceTemplate
				.marshalSendAndReceive(request);
		System.out.println("ResponseMessage: " + response.getMessage());
		return response.getMessage();
	}
}
