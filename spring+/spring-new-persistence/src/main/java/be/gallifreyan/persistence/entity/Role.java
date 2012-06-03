package be.gallifreyan.persistence.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Role implements Serializable {
	private static final long serialVersionUID = -5554853829437172284L;

	@Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}