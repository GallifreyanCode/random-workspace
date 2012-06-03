package be.gallifreyan.persistence.service;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import be.gallifreyan.api.EntityService;
import be.gallifreyan.logging.LogLevel;
import be.gallifreyan.logging.annotation.LoggableMethod;
import be.gallifreyan.persistence.entity.User;
import be.gallifreyan.persistence.repo.Repository;

@Service
public class UserService implements EntityService<User>, UserDetailsService {
	Repository<User> repository;

	@Inject
	public final void setRepository(final Repository<User> repository) {
		this.repository = repository;
	}

	public UserService() {
	}

	@LoggableMethod(LogLevel.ERROR)
	public User save(User user) {
		return repository.save(user);
	}

	public User findByName(String string) {
		return repository.findByName(string);
	}

	@PostConstruct
	public void initialize() {
		if (repository.findByName("user") == null) {
			repository.save(new User("user", "demo", "ROLE_USER"));
		}
	}

	@Override
	@LoggableMethod(LogLevel.ERROR)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = repository.findByName(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		return new org.springframework.security.core.userdetails.User(
				user.getName(), user.getPassword(),
				Collections.singleton(authority));
	}
}