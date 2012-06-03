package be.gallifreyan.ws.server.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import be.gallifreyan.ws.model.UserProfile;
import be.gallifreyan.ws.server.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Map<String, UserProfile> USERS = new HashMap<String, UserProfile>();

	public String createUser(UserProfile userProfile) {
		USERS.put(userProfile.getFirstName(), userProfile);
		return "user created successfully";
	}

}
