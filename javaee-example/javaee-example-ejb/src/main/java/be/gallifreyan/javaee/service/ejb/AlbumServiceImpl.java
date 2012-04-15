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
@EJB(name = "java:global/javaee-example/javaee-example-ejb/AlbumService", beanInterface = AlbumService.class)
@RolesAllowed({ "RegisteredUsers" })
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AlbumServiceImpl implements AlbumService
{
	private static final Logger logger = LoggerFactory.getLogger(AlbumServiceImpl.class);

	@Resource
	private SessionContext context;

	@EJB
	private UserRepository userRepository;

	@EJB
	private AlbumRepository albumRepository;

	@Override
	public Album createAlbum(Album album) throws AlbumException
	{
		validateAlbum(album);

		User user = findCurrentUser(CREATE_ALBUM_INTERNAL_ERROR);

		logger.debug("User's albums: {}", user.getAlbums());
		if (user.getAlbums().contains(album))
		{
			logger.error("The album to be created, already exists.");
			throw new AlbumException(DUPLICATE_ALBUM);
		}

		user.addToAlbums(album);
		Album createdAlbum = albumRepository.create(album);
		return createdAlbum;
	}

	@Override
	public Album modifyAlbum(Album album) throws AlbumException
	{
		validateAlbum(album);

		User user = findCurrentUser(MODIFY_ALBUM_INTERNAL_ERROR);

		if (user.getAlbums().contains(album))
		{
			logger.error("The album modification would have resulted in a possible duplicate.");
			throw new AlbumException(DUPLICATE_ALBUM_ON_MODIFY);
		}

		Album modifiedAlbum = null;
		for (Album anAlbum : user.getAlbums())
		{
			if (anAlbum.getAlbumId() == album.getAlbumId())
			{
				anAlbum.setName(album.getName());
				anAlbum.setDescription(album.getDescription());
				modifiedAlbum = albumRepository.modify(anAlbum);
				break;
			}
		}

		if (modifiedAlbum == null)
		{
			throw new AlbumException(MODIFY_ALBUM_INTERNAL_ERROR);
		}
		return modifiedAlbum;
	}

	@Override
	public void deleteAlbum(Album album) throws AlbumException
	{
		User user = findCurrentUser(DELETE_ALBUM_INTERNAL_ERROR);

		boolean deletedFlag = false;
		for (Album anAlbum : user.getAlbums())
		{
			if (anAlbum.equals(album))
			{
				logger.info("Flagging to delete album {}", anAlbum);
				albumRepository.delete(anAlbum);
				deletedFlag = true;
				break;
			}
		}

		if (deletedFlag == false)
		{
			throw new AlbumException(DELETE_ALBUM_INTERNAL_ERROR);
		}

		return;
	}

	@Override
	public List<Album> findCurrentUserAlbums() throws AlbumException
	{
		User user = findCurrentUser(FIND_ALL_ALBUMS_INTERNAL_ERROR);

		Set<Album> albumSet = user.getAlbums();
		List<Album> albums = new ArrayList<Album>(albumSet);
		return albums;
	}

	@Override
	public List<Album> findAllAlbumByOwner(User user) throws AlbumException
	{
		User userInRepository = userRepository.findById(user.getUserId());
		if (userInRepository == null)
		{
			throw new AlbumException(FIND_ALBUM_BY_OWNER_ALBUM_INTERNAL_ERROR);
		}
		else
		{
			List<Album> albums = albumRepository.findAllByOwner(user.getUserId());
			return albums;
		}
	}

	@Override
	public Album findAlbumById(long albumId) throws AlbumException
	{
		User user = findCurrentUser(FIND_ALBUM_BY_ID_INTERNAL_ERROR);

		Album album = null;
		for (Album anAlbum : user.getAlbums())
		{
			if (anAlbum.getAlbumId() == albumId)
			{
				album = anAlbum;
				break;
			}
		}
		if (album == null)
		{
			throw new AlbumException(ALBUM_NOT_FOUND);
		}

		return album;
	}

	/**
	 * @param album
	 * @throws AlbumException
	 */
	@SuppressWarnings("unchecked")
	private void validateAlbum(Album album) throws AlbumException
	{
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		@SuppressWarnings("rawtypes")
		Set violations = validator.validate(album);
		if (violations.size() > 0)
		{
			logger.warn("An invalid album entity was provided. Violations detected were {}", violations);
			throw new AlbumException(violations);
		}
	}

	/**
	 * @param contextExceptionMessage
	 *            The message to be contained in the AlbumException, if one were
	 *            to be thrown.
	 * @return
	 * @throws AlbumException
	 */
	private User findCurrentUser(String contextExceptionMessage) throws AlbumException
	{
		Principal caller = context.getCallerPrincipal();
		String userId = caller.getName();
		User user = userRepository.findById(userId);
		if (user == null)
		{
			logger.error("The principal for the caller was not found.");
			throw new AlbumException(contextExceptionMessage);
		}
		return user;
	}

}
