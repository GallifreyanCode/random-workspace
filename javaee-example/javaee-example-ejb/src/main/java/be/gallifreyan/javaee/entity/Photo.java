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
public class Photo implements Serializable {
	private static final long serialVersionUID = 2501648280091501264L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PHOTOS_SEQ")
	@TableGenerator(name = "PHOTOS_SEQ", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "PHOTOS_SEQ", valueColumnName = "SEQ_COUNT", allocationSize = 1)
	@Column(nullable = false)
	private long photoId;

	@Column(length = 255)
	@Size(min = 0, max = 255, message = "{Photo.description.size}")
	private String description;

	@Lob()
	@Basic(fetch = FetchType.LAZY)
	@Column(nullable = false)
	@NotNull
	private byte[] file;

	@Column(nullable = false, length = 255)
	@NotNull(message = "{Photo.fileName.notNull}")
	@Size(min = 1, max = 255, message = "{Photo.fileName.size}")
	private String fileName;

	@Column(length = 255)
	@Size(min = 0, max = 255, message = "{Photo.title.size}")
	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@NotNull(groups = PersistenceConstraint.class)
	private Date uploadTime;

	@ManyToOne(cascade = { CascadeType.DETACH })
	@JoinColumn(name = "ALBUMID", nullable = false)
	@NotNull(groups = PersistenceConstraint.class)
	private Album album;

	public long getPhotoId() {
		return this.photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getFile() {
		return this.file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Album getAlbum() {
		return this.album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public void modifyAlbum(final Album album) {
		Album currentAlbum = getAlbum();
		// check for no-op
		if (album == null || album.equals(currentAlbum)) {
			return;
		}
		// delegate to parent to associate
		album.addToPhotos(this);
		// additional business logic
		onModifyAlbum(currentAlbum, album);
	}

	public void clearAlbum() {
		Album currentAlbum = getAlbum();
		// check for no-op
		if (currentAlbum == null) {
			return;
		}
		// delegate to parent to dissociate
		currentAlbum.removeFromPhotos(this);
		// additional business logic
		onClearAlbum(currentAlbum);
	}

	protected void onModifyAlbum(final Album oldAlbum, final Album newAlbum) {
	}

	protected void onClearAlbum(final Album oldAlbum) {
	}

	protected Photo() {
	}

	public Photo(String fileName, byte[] file) {
		this.fileName = fileName;
		this.file = file;
	}

	public Photo(String fileName, byte[] file, String title, String description) {
		this(fileName, file);
		this.title = title;
		this.description = description;
	}

	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(file);
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Photo))
			return false;
		Photo other = (Photo) obj;
		return ((fileName == null ? other.fileName == null : fileName
				.equals(other.fileName)) && Arrays.equals(file, other.file));
	}

	public String toString() {
		return "Photo [fileName=" + fileName + ", album=" + album + "]";
	}
}
