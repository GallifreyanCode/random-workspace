package be.gallifreyan.javaee.entity;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "ALBUMS")
@NamedQueries({
		@NamedQuery(name = "Album.findAllAlbums", query = "SELECT a FROM Album a"),
		@NamedQuery(name = "Album.findAllAlbumsByOwner", query = "SELECT a FROM Album a JOIN a.user u WHERE u.userId = :userId") })
public class Album implements Serializable {
	private static final long serialVersionUID = -6934119700694912750L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ALBUMS_SEQ")
	@TableGenerator(name = "ALBUMS_SEQ", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "ALBUMS_SEQ", valueColumnName = "SEQ_COUNT", allocationSize = 1)
	@Column(nullable = false)
	private long albumId;

	@Column(length = 255)
	@Size(min = 0, max = 255, message = "{Album.description.size}")
	private String description;

	@Column(length = 50, nullable = false)
	@NotNull(message = "{Album.name.notNull}")
	@Size(min = 1, max = 50, message = "{Album.name.size}")
	private String name;

	@ManyToOne
	@JoinColumn(name = "USERID", nullable = false, updatable = false)
	private User user;

	@OneToMany(mappedBy = "album", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Photo> photos;

	@OneToOne
	@JoinColumn(name = "COVER_PHOTO", referencedColumnName = "PHOTOID")
	private Photo coverPhoto;

	public long getAlbumId() {
		return this.albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void modifyUser(final User user) {
		User currentUser = getUser();
		// check for no-op
		if (user == null || user.equals(currentUser)) {
			return;
		}
		// delegate to parent to associate
		user.addToAlbums(this);
		// additional business logic
		onModifyUser(currentUser, user);
	}

	public void clearUser() {
		User currentUser = getUser();
		// check for no-op
		if (currentUser == null) {
			return;
		}
		// delegate to parent to dissociate
		user.removeFromAlbums(this);
		// additional business logic
		onClearUser(currentUser);
	}

	protected void onModifyUser(final User oldUser, final User newUser) {
	}

	protected void onClearUser(final User oldUser) {
	}

	public Set<Photo> getPhotos() {
		return this.photos;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

	public void addToPhotos(final Photo photo) {
		// check for no-op
		if (photo == null || getPhotos().contains(photo)) {
			return;
		}
		// dissociate arg from existing parent if any
		photo.clearAlbum();
		// associate arg
		photo.setAlbum(this);
		getPhotos().add(photo);
		// additional business logic
		onAddToPhotos(photo);
	}

	public void removeFromPhotos(final Photo photo) {
		// check for no-op
		if (photo == null || !getPhotos().contains(photo)) {
			return;
		}
		// dissociate arg
		getPhotos().remove(photo);
		photo.setAlbum(null);
		// additional business logic
		onRemoveFromPhotos(photo);
	}

	protected void onAddToPhotos(final Photo photo) {
		if (getCoverPhoto() == null) {
			modifyCoverPhoto(photo);
		}
	}

	protected void onRemoveFromPhotos(final Photo photo) {
		if (photo.equals(getCoverPhoto())) {
			clearCoverPhoto();
			int noOfPhotos = getPhotos().size();

			if (noOfPhotos > 0) {
				double random = Math.random();
				int newCoverIndex = (int) (random * getPhotos().size());
				Photo newCover = getPhotos().toArray(new Photo[0])[newCoverIndex];
				modifyCoverPhoto(newCover);
			}
		}
	}

	public Photo getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(final Photo coverPhoto) {
		if (coverPhoto == null || getPhotos().contains(coverPhoto))
			this.coverPhoto = coverPhoto;
	}

	public void modifyCoverPhoto(final Photo coverPhoto) {
		Photo currentCoverPhoto = getCoverPhoto();
		// check for no-op
		if (coverPhoto == null || coverPhoto.equals(currentCoverPhoto)) {
			return;
		}
		// associate new
		setCoverPhoto(coverPhoto);
		// additional business logic
		onModifyCoverPhoto(currentCoverPhoto, coverPhoto);
	}

	public void clearCoverPhoto() {
		Photo currentCoverPhoto = getCoverPhoto();
		// check for no-op
		if (currentCoverPhoto == null) {
			return;
		}
		// dissociate existing
		setCoverPhoto(null);
		// additional business logic
		onClearCoverPhoto(currentCoverPhoto);
	}

	protected void onModifyCoverPhoto(final Photo oldCoverPhoto,
			final Photo newCoverPhoto) {
	}

	protected void onClearCoverPhoto(final Photo oldCoverPhoto) {
	}

	protected Album() {
		this.photos = new HashSet<Photo>();
	}

	public Album(String name, String description) {
		this();
		this.name = name;
		this.description = description;
	}

	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		return result;
	}

	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Album))
			return false;
		Album other = (Album) obj;
		return ((name == null ? other.name == null : name.equals(other.name)) && (description == null ? other.description == null
				: description.equals(other.description)));
	}

	public String toString() {
		return "Album [name=" + name + ", albumId=" + albumId + "]";
	}
}
