package be.gallifreyan.javaee.view.photo;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.application.*;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.Album;
import be.gallifreyan.javaee.entity.Photo;
import be.gallifreyan.javaee.i18n.Messages;
import be.gallifreyan.javaee.service.ejb.AlbumException;
import be.gallifreyan.javaee.service.ejb.ApplicationException;
import be.gallifreyan.javaee.service.ejb.AlbumService;
import be.gallifreyan.javaee.service.ejb.PhotoException;
import be.gallifreyan.javaee.service.ejb.PhotoService;
import be.gallifreyan.javaee.view.util.ExceptionProcessor;

@ManagedBean
@ViewScoped
public class PhotoManager
{
	private static final Logger logger = LoggerFactory.getLogger(PhotoManager.class);

	private UploadFileRequest uploadRequest;
	private EditPhotoRequest editRequest;

	@EJB
	private AlbumService albumService;

	@EJB
	private PhotoService photoService;

	private List<Album> albums;

	private Photo photo;

	private long photoId;

	private boolean isPhotoFetchAttempted;

	public PhotoManager()
	{
		this.uploadRequest = new UploadFileRequest();
		this.editRequest = new EditPhotoRequest();
	}

	@PostConstruct
	public void init()
	{
		try
		{
			albums = albumService.findCurrentUserAlbums();
		}
		catch (AlbumException albumEx)
		{
			logger.error("Failed to find the albums of the current user.", albumEx);
			populateErrorMessage(albumEx);
		}
	}

	public String uploadPhoto()
	{
		String result = null;
		try
		{
			Album selectedAlbum = null;
			for (Album album : albums)
			{
				if (album.getAlbumId() == uploadRequest.getAlbumId())
				{
					selectedAlbum = album;
					break;
				}
			}

			if (selectedAlbum != null)
			{
				UploadedFile file = uploadRequest.getFile();
				String fileName = file.getFileName();
				byte[] fileBytes = file.getContents();
				String photoTitle = uploadRequest.getTitle();
				String photoDescription = uploadRequest.getDescription();

				logger.info("Processed uploaded file {} of size {} for album {}", new Object[] { fileName,
						fileBytes.length, selectedAlbum });
				Photo photo = new Photo(fileName, fileBytes, photoTitle, photoDescription);
				photoService.uploadPhoto(photo, selectedAlbum);
				String messageKey = "UploadPhoto.PhotoUploadSuccessMessage";
				buildMessageForDisplay(messageKey, FacesMessage.SEVERITY_INFO);
				result = "/private/album/ViewAlbum.xhtml?faces-redirect=true&amp;albumId=" + selectedAlbum.getAlbumId();
			}
			else
			{
				String messageKey = "UploadPhoto.InvalidAlbumMessage";
				buildMessageForDisplay(messageKey, FacesMessage.SEVERITY_ERROR);
			}
		}
		catch (PhotoException photoEx)
		{
			logger.error("Failed to upload the photo", photoEx);
			populateErrorMessage(photoEx);
		}
		catch (EJBException ejbEx)
		{
			logger.error("An unexpected error was encountered when uploading the photo", ejbEx);
			String messageKey = "UploadPhoto.InternalErrorMessage";
			processContainerException(messageKey);
		}
		return result;
	}

	public String deletePhoto()
	{
		String result = null;
		try
		{
			long albumId = photo.getAlbum().getAlbumId();
			photoService.deletePhoto(photo);
			String messageKey = "DeletePhoto.DeletionSuccessMessage";
			buildMessageForDisplay(messageKey, FacesMessage.SEVERITY_INFO);
			result = "/private/album/ViewAlbum?faces-redirect=true&amp;albumId=" + albumId;
		}
		catch (PhotoException photoEx)
		{
			logger.error("Failed to delete the photo", photoEx);
			populateErrorMessage(photoEx);
		}
		return result;
	}

	public String editPhoto()
	{
		try
		{
			photo.setTitle(editRequest.getTitle());
			photo.setDescription(editRequest.getDescription());
			photoService.modifyPhoto(photo);
			String messageKey = "EditPhoto.PhotoModificationSuccessMessage";
			buildMessageForDisplay(messageKey, FacesMessage.SEVERITY_INFO);
		}
		catch (PhotoException albumEx)
		{
			logger.error("Failed to modify the photo.", albumEx);
			populateErrorMessage(albumEx);
		}
		catch (EJBException ejbEx)
		{
			logger.error("Failed to modify the photo.", ejbEx);
			String messageKey = "ModifyPhoto.InternalErrorMessage";
			processContainerException(messageKey);
		}
		return "/private/photo/ViewPhoto.xhtml?faces-redirect=true&amp;photoId=" + photoId;
	}

	public String setAsAlbumCover()
	{
		String result = null;
		try
		{
			photoService.setAlbumCover(photo);
			String messageKey = "SetAlbumCover.ModifySuccessMessage";
			buildMessageForDisplay(messageKey, FacesMessage.SEVERITY_INFO);
			result = "/private/photo/ViewPhoto.xhtml?faces-redirect=true&amp;photoId=" + photoId;
		}
		catch (PhotoException photoEx)
		{
			logger.error("Failed to set the photo as the album cover.", photoEx);
			populateErrorMessage(photoEx);
		}
		return result;
	}

	public UploadFileRequest getUploadRequest()
	{
		return uploadRequest;
	}

	public void setUploadRequest(UploadFileRequest uploadRequest)
	{
		this.uploadRequest = uploadRequest;
	}

	public EditPhotoRequest getEditRequest()
	{
		return editRequest;
	}

	public void setEditRequest(EditPhotoRequest editRequest)
	{
		this.editRequest = editRequest;
	}

	public List<Album> getAlbums()
	{
		return albums;
	}

	public void setAlbums(List<Album> albums)
	{
		this.albums = albums;
	}

	public Photo getPhoto()
	{
		return photo;
	}

	public void setPhoto(Photo photo)
	{
		this.photo = photo;
	}

	public long getPhotoId()
	{
		return photoId;
	}

	public void setPhotoId(long photoId)
	{
		this.photoId = photoId;
		logger.info("Value of the photo Id set to {}", photoId);
		if (!isPhotoFetchAttempted)
		{
			fetchPhoto();
		}

	}

	private void fetchPhoto()
	{
		isPhotoFetchAttempted = true;
		try
		{
			photo = photoService.findPhotoById(photoId, false);
			logger.info("Retrieved photo {} from the database", photo);
		}
		catch (PhotoException photoEx)
		{
			populateErrorMessage(photoEx);
		}
		catch (EJBException ejbEx)
		{
			String messageKey = "FindPhotoById.InternalErrorMessage";
			processContainerException(messageKey);
		}
	}

	/**
	 * @param messageKey
	 */
	private void processContainerException(String messageKey)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Locale locale = context.getViewRoot().getLocale();
		String message = Messages.getString(messageKey, locale);
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
		context.addMessage(null, facesMessage);
	}

	/**
	 * @param messageKey
	 * @param messageSeverity
	 */
	private void buildMessageForDisplay(String messageKey, Severity messageSeverity)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Locale locale = context.getViewRoot().getLocale();
		FacesMessage message = new FacesMessage(messageSeverity, Messages.getString(messageKey, locale), null);
		context.addMessage(null, message);
	}

	private void populateErrorMessage(ApplicationException exception)
	{
		ExceptionProcessor.populateErrorMessage(exception);
	}

}
