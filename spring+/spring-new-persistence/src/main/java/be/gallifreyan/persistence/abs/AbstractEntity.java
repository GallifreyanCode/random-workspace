package be.gallifreyan.persistence.abs;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;

import be.gallifreyan.api.Entity;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@MappedSuperclass
public abstract class AbstractEntity implements Entity, Serializable {
	private static final long serialVersionUID = 1751348457483396259L;
	
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
}
