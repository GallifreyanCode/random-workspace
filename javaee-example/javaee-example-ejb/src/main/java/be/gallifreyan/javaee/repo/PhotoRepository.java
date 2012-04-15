package be.gallifreyan.javaee.repo;


import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.persistence.*;

import be.gallifreyan.javaee.entity.Photo;
import be.gallifreyan.javaee.service.GenericRepository;

@Stateless
@LocalBean
@RolesAllowed({ "RegisteredUsers" })
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PhotoRepository implements GenericRepository<Photo, Long>
{

	@PersistenceContext
	private EntityManager em;

	public PhotoRepository()
	{
	}

	PhotoRepository(EntityManager em)
	{
		this.em = em;
	}

	@Override
	public Photo create(Photo photo)
	{
		em.persist(photo);
		return photo;
	}

	@Override
	public Photo modify(Photo photo)
	{
		em.find(Photo.class, photo.getPhotoId());
		Photo mergedPhoto = em.merge(photo);
		return mergedPhoto;
	}

	@Override
	public void delete(Photo photo)
	{
		Photo foundPhoto = em.find(Photo.class, photo.getPhotoId());
		foundPhoto.clearAlbum();
		em.remove(foundPhoto);
	}

	@Override
	public Photo findById(Long photoId)
	{
		Photo foundPhoto = em.find(Photo.class, photoId);
		return foundPhoto;
	}

	@Override
	public List<Photo> findAll()
	{
		TypedQuery<Photo> findAllPhotosQuery = em.createNamedQuery("Photo.findAllPhotos",
				Photo.class);
		List<Photo> photos = findAllPhotosQuery.getResultList();
		return photos;
	}

	public List<Photo> findPhotosByAlbum(long albumId)
	{
		TypedQuery<Photo> findAllPhotosQuery = em.createNamedQuery("Photo.findAllPhotosByAlbum",
				Photo.class);
		findAllPhotosQuery.setParameter("albumId", albumId);
		List<Photo> photos = findAllPhotosQuery.getResultList();
		return photos;
	}

}
