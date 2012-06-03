package be.gallifreyan.ws.server.endpoint;

import javax.inject.Inject;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import be.gallifreyan.ws.model.UserProfileCreateRequest;
import be.gallifreyan.ws.model.UserProfileCreateResponse;
import be.gallifreyan.ws.server.service.UserService;

@Endpoint
public class UserProfileEndpoint {
	private UserService service;

	@Inject
	public UserProfileEndpoint(UserService service) {
		this.service = service;
	}

	@PayloadRoot(localPart = "UserProfileCreateRequest", namespace = "http://gallifreyan.be/usermanagement/schemas")
	@ResponsePayload
	public UserProfileCreateResponse create(@RequestPayload UserProfileCreateRequest request) {
		String message = service.createUser(request.getUserProfile());
		UserProfileCreateResponse response = new UserProfileCreateResponse();
		response.setMessage(message);
		return response;

	}

}
