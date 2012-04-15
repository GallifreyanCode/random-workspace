package be.gallifreyan.javaee.service.ejb;

import java.util.List;

import be.gallifreyan.javaee.entity.*;

public interface AlbumService {

	public Album createAlbum(Album album) throws AlbumException;

	public Album modifyAlbum(Album album) throws AlbumException;

	public void deleteAlbum(Album album) throws AlbumException;

	public List<Album> findCurrentUserAlbums() throws AlbumException;

	public List<Album> findAllAlbumByOwner(User user) throws AlbumException;

	public Album findAlbumById(long albumId) throws AlbumException;

	public String CREATE_ALBUM_INTERNAL_ERROR = "CreateAlbum.InternalErrorMessage";

	public String DUPLICATE_ALBUM = "CreateAlbum.DuplicateAlbumMessage";

	public String DUPLICATE_ALBUM_ON_MODIFY = "ModifyAlbum.DuplicateAlbumMessage";

	public String MODIFY_ALBUM_INTERNAL_ERROR = "ModifyAlbum.InternalErrorMessage";

	public String DELETE_ALBUM_INTERNAL_ERROR = "DeleteAlbum.InternalErrorMessage";

	public String FIND_ALL_ALBUMS_INTERNAL_ERROR = "FindAllAlbums.InternalErrorMessage";

	public String FIND_ALBUM_BY_OWNER_ALBUM_INTERNAL_ERROR = "FindAlbumByUserId.InternalErrorMessage";

	public String FIND_ALBUM_BY_ID_INTERNAL_ERROR = "FindAlbumByAlbumId.InternalErrorMessage";

	public String ALBUM_NOT_FOUND = "FindAlbumByAlbumId.AlbumNotFoundMessage";
}
