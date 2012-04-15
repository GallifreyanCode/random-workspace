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
public class Album implements Serializable
{
	private static final long serialVersionUID = 1L;

	// {{ albumId
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ALBUMS_SEQ")
	@TableGenerator(name = "ALBUMS_SEQ", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "ALBUMS_SEQ", valueColumnName = "SEQ_COUNT", allocationSize = 1)
	@Column(nullable = false)
	private long albumId;

	/**
	 * Returns the auto-generated Id of the album.
	 * 
	 * @return The album's Id.
	 */
	public long getAlbumId()
	{
		return this.albumId;
	}

	/**
	 * Sets the Id of the album. This is meant to be invoked only by the JPA
	 * provider.
	 * 
	 * @param albumId
	 *            The album's Id.
	 */
	public void setAlbumId(long albumId)
	{
		this.albumId = albumId;
	}

	// }}

	// {{ description
	@Column(length = 255)
	@Size(min = 0, max = 255, message = "{Album.description.size}")
	private String description;

	/**
	 * Returns the album's description.
	 * 
	 * @return The album description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Sets the album's description.
	 * 
	 * @param description
	 *            The album's description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	// }}

	// {{ name
	@Column(length = 50, nullable = false)
	@NotNull(message = "{Album.name.notNull}")
	@Size(min = 1, max = 50, message = "{Album.name.size}")
	private String name;

	/**
	 * Returns the album's name.
	 * 
	 * @return The album's name.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the album's name.
	 * 
	 * @param name
	 *            The album's name.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	// }}

	// {{ user
	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "USERID", nullable = false, updatable = false)
	private User user;

	/**
	 * Returns the album's owner.
	 * 
	 * @return The album's owner
	 */
	public User getUser()
	{
		return this.user;
	}

	/**
	 * Sets the owner of the album. This method should not be invoked by a
	 * caller. Use {@link Album#modifyUser(User)} and {@link Album#clearUser()}
	 * to modify the album's owner.
	 * 
	 * @param user
	 *            The album's owner
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * Modifies the album's owner to the provided {@link User} instance.
	 * Duplicates and null instances are ignored by this method. The method is
	 * used to implement the mutual registration pattern between the
	 * {@link User} and {@link Album} classes.
	 * 
	 * @param user
	 *            The album's new owner
	 */
	public void modifyUser(final User user)
	{
		User currentUser = getUser();
		// check for no-op
		if (user == null || user.equals(currentUser))
		{
			return;
		}
		// delegate to parent to associate
		user.addToAlbums(this);
		// additional business logic
		onModifyUser(currentUser, user);
	}

	/**
	 * Clears the album's owner. The method is used to implement the mutual
	 * registration pattern between the {@link User} and {@link Album} classes.
	 */
	public void clearUser()
	{
		User currentUser = getUser();
		// check for no-op
		if (currentUser == null)
		{
			return;
		}
		// delegate to parent to dissociate
		user.removeFromAlbums(this);
		// additional business logic
		onClearUser(currentUser);
	}

	protected void onModifyUser(final User oldUser, final User newUser)
	{
	}

	protected void onClearUser(final User oldUser)
	{
	}

	// }}

	// {{ photos
	// bi-directional many-to-one association to Photo
	@OneToMany(mappedBy = "album", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Photo> photos;

	/**
	 * Returns the set of photos in the album.
	 * 
	 * @return
	 */
	public Set<Photo> getPhotos()
	{
		return this.photos;
	}

	/**
	 * Sets the set of photos in the album. This method should not be invoked by
	 * a caller. Use {@link Album#addToPhotos(Photo)} and
	 * {@link Album#removeFromPhotos(Photo)} to modify the photo collection.
	 * 
	 * @param photos
	 *            The set of photos in the album.
	 */
	public void setPhotos(Set<Photo> photos)
	{
		this.photos = photos;
	}

	/**
	 * Adds the photo to the set of photos. Duplicates and null instances are
	 * ignored by this method. The method is used to implement the mutual
	 * registration pattern between the {@link Album} and {@link Photo} classes.
	 * 
	 * @param photo
	 *            The photo to be added.
	 */
	public void addToPhotos(final Photo photo)
	{
		// check for no-op
		if (photo == null || getPhotos().contains(photo))
		{
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

	/**
	 * Removes the photo to the set of photos. Duplicates and null instances are
	 * ignored by this method. The method is used to implement the mutual
	 * registration pattern between the {@link Album} and {@link Photo} classes.
	 * 
	 * @param photo
	 *            The photo to be removed
	 */
	public void removeFromPhotos(final Photo photo)
	{
		// check for no-op
		if (photo == null || !getPhotos().contains(photo))
		{
			return;
		}
		// dissociate arg
		getPhotos().remove(photo);
		photo.setAlbum(null);
		// additional business logic
		onRemoveFromPhotos(photo);
	}

	/**
	 * Associates the cover photo of the album if the album has no cover photo.
	 * This will be true for the first photo and for the photo added to a clean
	 * album, and for albums that did not have a cover photo.
	 * 
	 * @param photo
	 *            The recently added Photo
	 */
	protected void onAddToPhotos(final Photo photo)
	{
		if (getCoverPhoto() == null)
		{
			modifyCoverPhoto(photo);
		}
	}

	/**
	 * Removes the association between the Album and the cover photo, if the
	 * cover photo is being deleted. A photo from the remaining set is randomly
	 * chosen as the cover photo.
	 * 
	 * @param photo
	 *            The recently removed Photo
	 */
	protected void onRemoveFromPhotos(final Photo photo)
	{
		if (photo.equals(getCoverPhoto()))
		{
			clearCoverPhoto();
			int noOfPhotos = getPhotos().size();
			if (noOfPhotos > 0)
			{
				double random = Math.random();
				int newCoverIndex = (int) (random * getPhotos().size());
				Photo newCover = getPhotos().toArray(new Photo[0])[newCoverIndex];
				modifyCoverPhoto(newCover);
			}
		}
	}

	// }}

	// {{ CoverPhoto
	@OneToOne
	@JoinColumn(name = "COVER_PHOTO", referencedColumnName = "PHOTOID")
	private Photo coverPhoto;

	/**
	 * Returns the cover photo for the Album. Returns null, if there are no
	 * Photos in the Album. Otherwise, this always returns a Photo.
	 * 
	 * @return The cover photo for the album
	 */
	public Photo getCoverPhoto()
	{
		return coverPhoto;
	}

	/**
	 * Sets the cover photo for the album. This should either be null, or a
	 * valid photo within the album.
	 * 
	 * This method should not be invoked by a caller. Use
	 * {@link Album#modifyCoverPhoto(Photo)} and {@link Album#clearCoverPhoto()}
	 * to modify the cover photo. Usually, even these methods are not required
	 * to be invoked except in special circumstances, as the
	 * {@link Album#addToPhotos(Photo)} and
	 * {@link Album#removeFromPhotos(Photo)} methods will take care of most
	 * scenarios.
	 * 
	 * @param coverPhoto
	 *            The cover photo of the Album
	 */
	public void setCoverPhoto(final Photo coverPhoto)
	{
		if (coverPhoto == null || getPhotos().contains(coverPhoto))
			this.coverPhoto = coverPhoto;
	}

	/**
	 * Modifies the album's cover photo to the provided {@link Photo} instance.
	 * Duplicates and null instances are ignored by this method. The Photo must
	 * also be a valid photo in the album.
	 * 
	 * @param coverPhoto
	 */
	public void modifyCoverPhoto(final Photo coverPhoto)
	{
		Photo currentCoverPhoto = getCoverPhoto();
		// check for no-op
		if (coverPhoto == null || coverPhoto.equals(currentCoverPhoto))
		{
			return;
		}
		// associate new
		setCoverPhoto(coverPhoto);
		// additional business logic
		onModifyCoverPhoto(currentCoverPhoto, coverPhoto);
	}

	/**
	 * Clears the album's cover photo.
	 */
	public void clearCoverPhoto()
	{
		Photo currentCoverPhoto = getCoverPhoto();
		// check for no-op
		if (currentCoverPhoto == null)
		{
			return;
		}
		// dissociate existing
		setCoverPhoto(null);
		// additional business logic
		onClearCoverPhoto(currentCoverPhoto);
	}

	protected void onModifyCoverPhoto(final Photo oldCoverPhoto, final Photo newCoverPhoto)
	{
	}

	protected void onClearCoverPhoto(final Photo oldCoverPhoto)
	{
	}

	// }}

	/**
	 * The default constructor. This is meant to be invoked directly only in
	 * unit tests.
	 */
	protected Album()
	{
		this.photos = new HashSet<Photo>();
	}

	/**
	 * Creates a new Album instance with the provided name and description.
	 * 
	 * @param name
	 *            The album's name
	 * @param description
	 *            The album's description
	 */
	public Album(String name, String description)
	{
		this();
		this.name = name;
		this.description = description;
	}

	@Override
	public final int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Album))
			return false;
		Album other = (Album) obj;
		return ((name == null ? other.name == null : name.equals(other.name)) && (description == null ? other.description == null
				: description.equals(other.description)));
	}

	@Override
	public String toString()
	{
		return "Album [name=" + name + ", albumId=" + albumId + "]";
	}

}