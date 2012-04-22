package be.gallifreyan.javaee.view.photo;

import java.io.*;

import javax.ejb.*;
import javax.faces.bean.*;
import javax.faces.context.*;

import org.primefaces.model.*;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.Photo;
import be.gallifreyan.javaee.service.ejb.PhotoException;
import be.gallifreyan.javaee.service.ejb.PhotoService;

@ManagedBean
@RequestScoped
public class PhotoStreamer
{
	private static final Logger logger = LoggerFactory.getLogger(PhotoStreamer.class);

	@EJB
	private PhotoService photoService;
	private long photoId;
	private StreamedContent fileContent;

	public PhotoStreamer()
	{
	}

	public long getPhotoId()
	{
		return photoId;
	}

	public void setPhotoId(long photoId)
	{
		this.photoId = photoId;
		logger.info("Value of the album Id set to {}", photoId);
	}

	public StreamedContent getFileContent()
	{
		logger.trace("Entered method getFileContent.");
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String photoId = externalContext.getRequestParameterMap().get("photo_id");
		if (photoId == null || photoId.equals("") || photoId.equals("null"))
		{
			fileContent = getDefaultContent();
			logger.info("Id was null or empty. Retrieved default file content.");
		}
		else
		{
			try
			{
				long parsedId = Long.parseLong(photoId);
				Photo photo = photoService.findPhotoById(parsedId, true);
				byte[] photoContents = photo.getFile();
				InputStream inputStream = new ByteArrayInputStream(photoContents);
				fileContent = new DefaultStreamedContent(inputStream, "image/png");
				logger.info("Retrieved file content for image {}.", parsedId);
			}
			catch (PhotoException photoEx)
			{
				logger.error("Failed to retrieve the photo.", photoEx);
			}
			catch (EJBException ejbEx)
			{
				logger.error("Failed to retrieve the photo.", ejbEx);
			}

		}
		logger.trace("Exited method getFileContent.");
		return fileContent;
	}

	private StreamedContent getDefaultContent()
	{
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = contextClassLoader.getResourceAsStream("resources/StandardIcon.png");
		DefaultStreamedContent defaultFileContent = new DefaultStreamedContent(inputStream, "image/png");
		return defaultFileContent;
	}

	public void setFileContent(StreamedContent fileContent)
	{
		this.fileContent = fileContent;
	}
}
