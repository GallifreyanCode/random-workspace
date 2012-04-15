package be.gallifreyan.javaee.service;

import java.util.List;

public interface GenericRepository<E, I> {
	public E create(E entity);

	public E modify(E entity);

	public void delete(E entity);

	public E findById(I id);

	public List<E> findAll();
}
