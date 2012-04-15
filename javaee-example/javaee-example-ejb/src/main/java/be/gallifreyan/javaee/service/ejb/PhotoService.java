package be.gallifreyan.javaee.service.ejb;

import java.util.List;

import be.gallifreyan.javaee.entity.*;

public interface PhotoService {

	public Photo uploadPhoto(Photo photo, Album album) throws PhotoException;

	public Photo modifyPhoto(Photo photo) throws PhotoException;

	public void deletePhoto(Photo photo) throws PhotoException;

	public Photo findPhotoById(long photoId, boolean withPhotoContent)
			throws PhotoException;

	public List<Photo> findPhotosByAlbum(Album album) throws PhotoException;

	public void setAlbumCover(Photo photo) throws PhotoException;

	public static final String DUPLICATE_PHOTO_ON_UPLOAD = "UploadPhoto.DuplicatePhotoMessage";

	public static final String UPLOAD_PHOTO_INTERNAL_ERROR_MESSAGE = "UploadPhoto.InternalErrorMessage";

	public static final String MODIFY_PHOTO_INTERNAL_ERROR_MESSAGE = "ModifyPhoto.InternalErrorMessage";

	public static final String DELETE_PHOTO_SUCCESS_MESSAGE = "DeletePhoto.DeletionSuccessMessage";

	public static final String DELETE_PHOTO_INTERNAL_ERROR_MESSAGE = "DeletePhoto.InternalErrorMessage";

	public static final String FIND_PHOTO_BY_ID_INTERNAL_ERROR_MESSAGE = "FindPhotoById.InternalErrorMessage";

	public static final String MODIFY_COVER_INTERNAL_ERROR_MESSAGE = "SetAlbumCover.InternalErrorMessage";
}
