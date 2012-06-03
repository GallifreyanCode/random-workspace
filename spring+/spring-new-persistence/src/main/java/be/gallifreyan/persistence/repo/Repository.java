package be.gallifreyan.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import be.gallifreyan.api.Entity;

@NoRepositoryBean
public interface Repository<T extends Entity> extends JpaSpecificationExecutor<T>, JpaRepository<T, Long> {
	T findByName(final String name);
}
