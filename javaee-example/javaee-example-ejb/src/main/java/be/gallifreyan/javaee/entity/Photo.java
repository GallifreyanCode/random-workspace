package be.gallifreyan.javaee.entity;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "PHOTOS")
@NamedQueries({
		@NamedQuery(name = "Photo.findAllPhotos", query = "SELECT p FROM Photo p"),
		@NamedQuery(name = "Photo.findAllPhotosByAlbum", query = "SELECT p FROM Photo p JOIN p.album a WHERE a.albumId = :albumId") })
public class Photo implements Serializable
{
	private static final long serialVersionUID = 1L;

	// {{ photoId
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PHOTOS_SEQ")
	@TableGenerator(name = "PHOTOS_SEQ", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "PHOTOS_SEQ", valueColumnName = "SEQ_COUNT", allocationSize = 1)
	@Column(nullable = false)
	private long photoId;

	/**
	 * Returns the auto-generated photo Id.
	 * 
	 * @return The photo's Id.
	 */
	public long getPhotoId()
	{
		return this.photoId;
	}

	/**
	 * Sets the Id of the photo. This is meant to be invoked only by the JPA
	 * provider.
	 * 
	 * @param photoId
	 *            The photo's Id.
	 */
	public void setPhotoId(long photoId)
	{
		this.photoId = photoId;
	}

	// }}

	// {{ description
	@Column(length = 255)
	@Size(min = 0, max = 255, message = "{Photo.description.size}")
	private String description;

	/**
	 * Returns the photo's description.
	 * 
	 * @return The description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Sets the description of the photo.
	 * 
	 * @param description
	 *            The description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	// }}

	// {{ file
	@Lob()
	@Basic(fetch = FetchType.LAZY)
	@Column(nullable = false)
	@NotNull
	private byte[] file;

	/**
	 * Returns the file contents of the Photo.
	 * 
	 * @return The file contents
	 */
	public byte[] getFile()
	{
		return this.file;
	}

	/**
	 * Sets the file contents of the Photo. This method is typically not called
	 * by a client, as users are expected to delete and upload new Photos.
	 * 
	 * @param file
	 *            The file contents
	 */
	public void setFile(byte[] file)
	{
		this.file = file;
	}

	// }}

	// {{ fileName
	@Column(nullable = false, length = 255)
	@NotNull(message = "{Photo.fileName.notNull}")
	@Size(min = 1, max = 255, message = "{Photo.fileName.size}")
	private String fileName;

	/**
	 * Returns the photos' file name.
	 * 
	 * @return The photo's file name.
	 */
	public String getFileName()
	{
		return this.fileName;
	}

	/**
	 * Sets the photo's file name. This is typically not invoked by a caller, as
	 * end-users will not be changing the file name of the photo after it is
	 * uploaded.
	 * 
	 * @param fileName
	 *            The photo's file name.
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	// }}

	// {{ title
	@Column(length = 255)
	@Size(min = 0, max = 255, message = "{Photo.title.size}")
	private String title;

	/**
	 * Returns the title of the photo.
	 * 
	 * @return The photo's title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * Sets the title of the photo.
	 * 
	 * @param title
	 *            The photo's title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	// }}

	// {{ uploadTime
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@NotNull(groups = PersistenceConstraint.class)
	private Date uploadTime;

	/**
	 * Returns the time of upload of the photo.
	 * 
	 * @return The time of upload
	 */
	public Date getUploadTime()
	{
		return this.uploadTime;
	}

	/**
	 * Sets the time of upload of the photo. This is typically invoked by the
	 * caller only once.
	 * 
	 * @param uploadTime
	 *            The time of upload.
	 */
	public void setUploadTime(Date uploadTime)
	{
		this.uploadTime = uploadTime;
	}

	// }}

	// {{ album
	// bi-directional many-to-one association to Album
	@ManyToOne(cascade = { CascadeType.DETACH })
	@JoinColumn(name = "ALBUMID", nullable = false)
	@NotNull(groups = PersistenceConstraint.class)
	private Album album;

	/**
	 * Returns the album that the photo belongs to.
	 * 
	 * @return The photo's album
	 */
	public Album getAlbum()
	{
		return this.album;
	}

	/**
	 * Sets the album that the photo belongs to. This method should not be
	 * invoked by a caller. Use {@link Photo#modifyAlbum(Album)} and
	 * {@link Photo#clearAlbum()} to modify the photo's album.
	 * 
	 * @param album
	 */
	public void setAlbum(Album album)
	{
		this.album = album;
	}

	/**
	 * Modifies the photo's album to the provided {@link Album} instance.
	 * Duplicates and null instances are ignored by this method. The method is
	 * used to implement the mutual registration pattern between the
	 * {@link Photo} and {@link Album} classes.
	 * 
	 * @param album
	 *            The photo's new album
	 */
	public void modifyAlbum(final Album album)
	{
		Album currentAlbum = getAlbum();
		// check for no-op
		if (album == null || album.equals(currentAlbum))
		{
			return;
		}
		// delegate to parent to associate
		album.addToPhotos(this);
		// additional business logic
		onModifyAlbum(currentAlbum, album);
	}

	/**
	 * Clears the photo's album. The method is used to implement the mutual
	 * registration pattern between the {@link Photo} and {@link Album} classes.
	 */
	public void clearAlbum()
	{
		Album currentAlbum = getAlbum();
		// check for no-op
		if (currentAlbum == null)
		{
			return;
		}
		// delegate to parent to dissociate
		currentAlbum.removeFromPhotos(this);
		// additional business logic
		onClearAlbum(currentAlbum);
	}

	protected void onModifyAlbum(final Album oldAlbum, final Album newAlbum)
	{
	}

	protected void onClearAlbum(final Album oldAlbum)
	{
	}

	// }}

	/**
	 * The default constructor. This is meant to be invoked directly only in
	 * unit tests.
	 */
	protected Photo()
	{
	}

	/**
	 * Creates a new instance of a Photo populated only with the file name and
	 * contents.
	 * 
	 * @param fileName
	 *            The file name of the Photo.
	 * @param file
	 *            The contents of the Photo.
	 */
	public Photo(String fileName, byte[] file)
	{
		this.fileName = fileName;
		this.file = file;
	}

	/**
	 * Creates a new instance of a Photo populated with the file name, contents,
	 * title and description.
	 * 
	 * @param fileName
	 *            The file name of the Photo.
	 * @param file
	 *            The contents of the Photo.
	 * @param title
	 *            The photo's title
	 * @param description
	 *            The photo's description
	 */
	public Photo(String fileName, byte[] file, String title, String description)
	{
		this(fileName, file);
		this.title = title;
		this.description = description;
	}

	@Override
	public final int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(file);
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Photo))
			return false;
		Photo other = (Photo) obj;
		return ((fileName == null ? other.fileName == null : fileName.equals(other.fileName)) && Arrays.equals(file,
				other.file));
	}

	@Override
	public String toString()
	{
		return "Photo [fileName=" + fileName + ", album=" + album + "]";
	}

}