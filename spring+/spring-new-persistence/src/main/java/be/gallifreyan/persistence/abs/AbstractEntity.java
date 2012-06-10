package be.gallifreyan.persistence.abs;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.gallifreyan.api.Entity;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@MappedSuperclass
public abstract class AbstractEntity implements Entity, Serializable {
	private static final long serialVersionUID = 1751348457483396259L;
	@Transient
	private static final Logger logger = LoggerFactory.getLogger(AbstractEntity.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Version
	private Integer version;
	private String name;

	public AbstractEntity() {

	}

	public AbstractEntity(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@PostConstruct
	public void getLogger() {
		
	}
	
	@PrePersist
	public void prePersist() {
		logger.info("@PrePersist");
	}
	
	@PreRemove
	public void preRemove() {
		logger.info("@PreRemove");
	}
	
	@PreUpdate
	public void preUpdate() {
		logger.info("@PreUpdate");
	}
	
	@PostPersist
	public void postPersist() {
		logger.info("@PostPersist");
	}
	
	@PostRemove
	public void postRemove() {
		logger.info("@PostRemove");
	}
	
	@PostUpdate
	public void postUpdate() {
		logger.info("@PostUpdate");
	}
}
