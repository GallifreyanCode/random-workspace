package be.gallifreyan.javaee.repo;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.persistence.*;

import be.gallifreyan.javaee.entity.Album;
import be.gallifreyan.javaee.service.GenericRepository;

@Stateless
@LocalBean
@RolesAllowed({ "RegisteredUsers" })
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AlbumRepository implements GenericRepository<Album, Long> {
	@PersistenceContext
	private EntityManager em;

	public AlbumRepository() {
	}

	AlbumRepository(EntityManager em) {
		this.em = em;
	}

	@Override
	public Album create(Album album) {
		em.persist(album);
		return album;
	}

	@Override
	public Album modify(Album album) {
		em.find(Album.class, album.getAlbumId());
		Album mergedAlbum = em.merge(album);
		return mergedAlbum;
	}

	@Override
	public void delete(Album album) {
		Album foundAlbum = em.find(Album.class, album.getAlbumId());
		foundAlbum.clearUser();
		em.remove(foundAlbum);
	}

	@Override
	public Album findById(Long albumId) {
		Album album = em.find(Album.class, albumId);
		return album;
	}

	@Override
	public List<Album> findAll() {
		TypedQuery<Album> findAllAlbumsQuery = em.createNamedQuery(
				"Album.findAllAlbums", Album.class);
		List<Album> resultList = findAllAlbumsQuery.getResultList();
		return resultList;
	}

	public List<Album> findAllByOwner(String userId) {
		TypedQuery<Album> findAllAlbumsQuery = em.createNamedQuery(
				"Album.findAllAlbumsByOwner", Album.class);
		findAllAlbumsQuery.setParameter("userId", userId);
		List<Album> resultList = findAllAlbumsQuery.getResultList();
		return resultList;
	}

	public Album findByOwnerAndId(String userId, Long albumId) {
		TypedQuery<Album> findAlbumQuery = em.createNamedQuery(
				"Album.findAlbumByOwnerAndId", Album.class);
		findAlbumQuery.setParameter("userId", userId);
		findAlbumQuery.setParameter("albumId", albumId);
		Album album = findAlbumQuery.getSingleResult();
		return album;
	}
}
