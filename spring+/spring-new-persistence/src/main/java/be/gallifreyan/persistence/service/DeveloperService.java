package be.gallifreyan.persistence.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import be.gallifreyan.api.EntityService;
import be.gallifreyan.logging.annotation.LoggableClass;
import be.gallifreyan.persistence.entity.Developer;
import be.gallifreyan.persistence.repo.Repository;

@Service
@LoggableClass
public class DeveloperService implements EntityService<Developer> {
	Repository<Developer> repository;

	@Inject
	public final void setRepository(final Repository<Developer> repository) {
		this.repository = repository;
	}

	public Developer save(Developer entity) {
		return repository.save(entity);
	}

	public Developer findByName(String name) {
		return repository.findByName(name);
	}
	
	public List<Developer> findAll() {
		return repository.findAll();
	}
}