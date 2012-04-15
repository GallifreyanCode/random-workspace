package be.gallifreyan.javaee.service.ejb;


import java.security.Principal;
import java.util.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.validation.*;

import org.slf4j.*;

import be.gallifreyan.javaee.entity.*;
import be.gallifreyan.javaee.repo.*;

@Stateless
@EJB(name = "java:global/javaee-example/javaee-example-ejb/PhotoService", beanInterface = PhotoService.class)
@RolesAllowed({ "RegisteredUsers" })
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PhotoServiceImpl implements PhotoService {
	private static final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

	@Resource private SessionContext context;
	@EJB private AlbumService albumService;
	@EJB private UserRepository userRepository;
	@EJB private AlbumRepository albumRepository;
	@EJB private PhotoRepository photoRepository;

	public Photo uploadPhoto(Photo photo, Album album) throws PhotoException {
		validatePhoto(photo);

		User user = findCurrentUser(UPLOAD_PHOTO_INTERNAL_ERROR_MESSAGE);
		Album albumInRepository = albumRepository.findById(album.getAlbumId());
		User albumOwner = albumInRepository.getUser();
		if (!user.equals(albumOwner))
		{
			throw new PhotoException(UPLOAD_PHOTO_INTERNAL_ERROR_MESSAGE);
		}
		else if (albumInRepository.getPhotos().contains(photo))
		{
			throw new PhotoException(DUPLICATE_PHOTO_ON_UPLOAD);
		}
		else
		{
			photo.setUploadTime(new Date());
			photo.modifyAlbum(albumInRepository);
			Photo createdPhoto = photoRepository.create(photo);
			return createdPhoto;
		}
	}

	public Photo modifyPhoto(Photo photo) throws PhotoException {
		validatePhoto(photo);

		User user = findCurrentUser(MODIFY_PHOTO_INTERNAL_ERROR_MESSAGE);
		Photo foundPhoto = photoRepository.findById(photo.getPhotoId());
		User photoOwner = foundPhoto.getAlbum().getUser();
		if (user.equals(photoOwner))
		{
			foundPhoto.setTitle(photo.getTitle());
			foundPhoto.setDescription(photo.getDescription());
			Photo modifiedPhoto = photoRepository.modify(foundPhoto);
			return modifiedPhoto;
		}
		else
		{
			throw new PhotoException(MODIFY_PHOTO_INTERNAL_ERROR_MESSAGE);
		}
	}

	public void deletePhoto(Photo photo) throws PhotoException {
		User user = findCurrentUser(DELETE_PHOTO_INTERNAL_ERROR_MESSAGE);
		Photo foundPhoto = photoRepository.findById(photo.getPhotoId());
		User photoOwner = foundPhoto.getAlbum().getUser();
		if (user.equals(photoOwner))
		{
			logger.info("Flagging to delete photo {}", foundPhoto);
			photoRepository.delete(foundPhoto);
			return;
		}
		else
		{
			throw new PhotoException(DELETE_PHOTO_INTERNAL_ERROR_MESSAGE);
		}
	}

	public Photo findPhotoById(long photoId, boolean withContents) throws PhotoException {
		User user = findCurrentUser(FIND_PHOTO_BY_ID_INTERNAL_ERROR_MESSAGE);
		Photo foundPhoto = photoRepository.findById(photoId);
		User photoOwner = foundPhoto.getAlbum().getUser();
		if (user.equals(photoOwner))
		{
			if (withContents)
			{
				int length = foundPhoto.getFile().length;
				logger.info("Returning photo {} with content of size {} matching Id {}", new Object[] { foundPhoto,
						length, photoId });
				return foundPhoto;
			}
			else
			{
				logger.info("Returning photo {} matching Id {}", new Object[] { foundPhoto, photoId });
				return foundPhoto;
			}
		}
		else
		{
			throw new PhotoException(FIND_PHOTO_BY_ID_INTERNAL_ERROR_MESSAGE);
		}
	}

	public List<Photo> findPhotosByAlbum(Album album) throws PhotoException {
		try
		{
			Album foundAlbum = albumService.findAlbumById(album.getAlbumId());
			Set<Photo> photoSet = foundAlbum.getPhotos();
			List<Photo> photos = new ArrayList<Photo>(photoSet);
			return photos;
		}
		catch (AlbumException albumEx)
		{
			throw new PhotoException(albumEx);
		}
	}

	public void setAlbumCover(Photo photo) throws PhotoException {
		validatePhoto(photo);

		User user = findCurrentUser(MODIFY_COVER_INTERNAL_ERROR_MESSAGE);
		Album album = albumRepository.findById(photo.getAlbum().getAlbumId());
		User albumOwner = album.getUser();
		if (!user.equals(albumOwner))
		{
			throw new PhotoException(MODIFY_COVER_INTERNAL_ERROR_MESSAGE);
		}
		else if (!album.getPhotos().contains(photo))
		{
			throw new PhotoException(MODIFY_COVER_INTERNAL_ERROR_MESSAGE);
		}
		else
		{
			album.modifyCoverPhoto(photo);
			albumRepository.modify(album);
		}
	}

	@SuppressWarnings("unchecked")
	private void validatePhoto(Photo photo) throws PhotoException {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		@SuppressWarnings("rawtypes")
		Set violations = validator.validate(photo);
		if (violations.size() > 0)
		{
			logger.warn("An invalid album entity was provided. Violations detected were {}", violations);
			throw new PhotoException(violations);
		}
	}

	/**
	 * @param contextExceptionMessage
	 *            The message to be contained in the AlbumException, if one were
	 *            to be thrown.
	 * @return
	 * @throws PhotoException
	 */
	private User findCurrentUser(String contextExceptionMessage) throws PhotoException {
		Principal caller = context.getCallerPrincipal();
		String userId = caller.getName();
		User user = userRepository.findById(userId);
		if (user == null)
		{
			logger.error("The principal for the caller was not found.");
			throw new PhotoException(contextExceptionMessage);
		}
		return user;
	}

}
