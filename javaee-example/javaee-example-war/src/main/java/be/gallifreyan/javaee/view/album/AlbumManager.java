package be.gallifreyan.javaee.view.album;

import java.io.IOException;
import java.util.*;

import javax.ejb.*;
import javax.faces.application.*;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.*;
import javax.faces.context.*;

import org.slf4j.*;

import be.gallifreyan.javaee.entity.Album;
import be.gallifreyan.javaee.entity.Photo;
import be.gallifreyan.javaee.i18n.Messages;
import be.gallifreyan.javaee.service.ejb.AlbumException;
import be.gallifreyan.javaee.service.ejb.ApplicationException;
import be.gallifreyan.javaee.service.ejb.AlbumService;
import be.gallifreyan.javaee.service.ejb.PhotoService;
import be.gallifreyan.javaee.view.util.ExceptionProcessor;

@ManagedBean
@ViewScoped
public class AlbumManager
{
	private static final Logger logger = LoggerFactory.getLogger(AlbumManager.class);

	private CreateAlbumRequest createRequest;
	private EditAlbumRequest editRequest;

	@EJB
	private AlbumService albumService;

	@EJB
	private PhotoService photoService;

	private Album currentAlbum;

	private long currentAlbumId;

	private List<Photo> photosInCurrentAlbum;
	private boolean init;

	private List<Album> ownedAlbums;

	public AlbumManager()
	{
		photosInCurrentAlbum = new ArrayList<Photo>();
		createRequest = new CreateAlbumRequest();
		editRequest = new EditAlbumRequest();
		ownedAlbums = new ArrayList<Album>();
	}

	public String createAlbum()
	{
		String result = null;
		try
		{
			String name = createRequest.getName();
			String description = createRequest.getDescription();
			Album album = new Album(name, description);
			albumService.createAlbum(album);
			String key = "CreateAlbum.AlbumCreationSuccessMessage";
			buildMessageForDisplay(key, FacesMessage.SEVERITY_INFO);
			result = "/private/HomePage.xhtml?faces-redirect=true";
		}
		catch (AlbumException albumEx)
		{
			populateErrorMessage(albumEx);
		}
		catch (EJBException ejbEx)
		{
			String messageKey = "CreateAlbum.InternalFailureMessage";
			logger.error(Messages.getLoggerString(messageKey), ejbEx);
			processContainerException(messageKey);
		}
		return result;
	}

	public void fetchOwnedAlbums()
	{
		init = true;
		try
		{
			ownedAlbums = albumService.findCurrentUserAlbums();
		}
		catch (AlbumException albumEx)
		{
			logger.error("Failed to obtain the list of albums", albumEx);
			populateErrorMessage(albumEx);
		}
		catch (EJBException ejbEx)
		{
			logger.error("Unpredicted error occurred.", ejbEx);
			String messageKey = "FindAllAlbums.InternalErrorMessage";
			processContainerException(messageKey);
		}
		return;
	}

	public String deleteAlbum()
	{
		try
		{
			albumService.deleteAlbum(currentAlbum);
			String messageKey = "ViewAlbum.AlbumDeletionSuccessMessage";
			buildMessageForDisplay(messageKey, FacesMessage.SEVERITY_INFO);
		}
		catch (AlbumException albumEx)
		{
			logger.error("Failed to delete the album.", albumEx);
			populateErrorMessage(albumEx);
		}
		catch (EJBException ejbEx)
		{
			logger.error("Failed to delete the album.", ejbEx);
			String messageKey = "DeleteAlbum.InternalErrorMessage";
			processContainerException(messageKey);
		}
		return "/private/HomePage.xhtml?faces-redirect=true";
	}

	public String editAlbum()
	{
		String result = null;
		try
		{
			currentAlbum.setName(editRequest.getName());
			currentAlbum.setDescription(editRequest.getDescription());
			albumService.modifyAlbum(currentAlbum);
			String messageKey = "EditAlbum.AlbumModificationSuccessMessage";
			buildMessageForDisplay(messageKey, FacesMessage.SEVERITY_INFO);
			result = "/private/album/ViewAlbum.xhtml?faces-redirect=true&amp;albumId=" + currentAlbumId;
		}
		catch (AlbumException albumEx)
		{
			logger.error("Failed to modify the album.", albumEx);
			populateErrorMessage(albumEx);
		}
		catch (EJBException ejbEx)
		{
			logger.error("Failed to modify the album.", ejbEx);
			String messageKey = "ModifyAlbum.InternalErrorMessage";
			processContainerException(messageKey);
		}
		return result;
	}

	public List<Album> getOwnedAlbums()
	{
		if (!init)
		{
			fetchOwnedAlbums();
		}
		return ownedAlbums;
	}

	public void setOwnedAlbums(List<Album> ownedAlbums)
	{
		this.ownedAlbums = ownedAlbums;
	}

	public long getCurrentAlbumId()
	{
		return currentAlbumId;
	}

	public void setCurrentAlbumId(long currentAlbumId)
	{
		this.currentAlbumId = currentAlbumId;
		logger.info("Value of the album Id set to {}", currentAlbumId);
		if (!init)
		{
			initBean();
		}
	}

	public Album getCurrentAlbum()
	{
		return currentAlbum;
	}

	public void setCurrentAlbum(Album currentAlbum)
	{
		this.currentAlbum = currentAlbum;
	}

	public List<Photo> getPhotosInCurrentAlbum()
	{
		return photosInCurrentAlbum;
	}

	public void setPhotosInCurrentAlbum(List<Photo> photosInCurrentAlbum)
	{
		this.photosInCurrentAlbum = photosInCurrentAlbum;
	}

	public CreateAlbumRequest getCreateRequest()
	{
		return createRequest;
	}

	public void setCreateRequest(CreateAlbumRequest createRequest)
	{
		this.createRequest = createRequest;
	}

	public EditAlbumRequest getEditRequest()
	{
		return editRequest;
	}

	public void setEditRequest(EditAlbumRequest editRequest)
	{
		this.editRequest = editRequest;
	}

	private void initBean()
	{
		init = true;
		try
		{
			currentAlbum = albumService.findAlbumById(currentAlbumId);
			logger.info("Retrieved album {} from the database", currentAlbum);
			photosInCurrentAlbum = photoService.findPhotosByAlbum(currentAlbum);
			logger.info("Retrieved photos {} from the database", photosInCurrentAlbum);
		}
		catch (ApplicationException albumEx)
		{
			logger.error("Failed to retrieve album.", albumEx);
			populateErrorMessage(albumEx);
			redirectToMainPage();
		}
		catch (EJBException ejbEx)
		{
			logger.error("Failed to retrieve photos.", ejbEx);
			String messageKey = "FindAlbumByAlbumId.InternalErrorMessage";
			processContainerException(messageKey);
			redirectToMainPage();
		}
	}

	private void redirectToMainPage()
	{
		try
		{
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			String contextPath = externalContext.getRequestContextPath();
			externalContext.redirect(contextPath + "/private/HomePage.xhtml");
		}
		catch (IOException ioEx)
		{
			logger.error("Failed to redirect the user to the main page.", ioEx);
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
