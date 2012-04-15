package be.gallifreyan.javaee.repo;


import java.util.List;

import javax.ejb.*;
import javax.persistence.*;

import be.gallifreyan.javaee.entity.User;
import be.gallifreyan.javaee.service.GenericRepository;

@Stateless
@LocalBean
public class UserRepository implements GenericRepository<User, String>
{
	@PersistenceContext
	private EntityManager em;

	public UserRepository()
	{
	}

	UserRepository(EntityManager em)
	{
		this.em = em;
	}

	@Override
	public User create(User user)
	{
		em.persist(user);
		return user;
	}

	@Override
	public User modify(User user)
	{
		em.find(User.class, user.getUserId());
		User mergedUser = em.merge(user);
		return mergedUser;
	}

	@Override
	public void delete(User user)
	{
		User mergedUser = em.merge(user);
		em.remove(mergedUser);
	}

	@Override
	public User findById(String userId)
	{
		User user = em.find(User.class, userId);
		return user;
	}

	@Override
	public List<User> findAll()
	{
		TypedQuery<User> findAllUsersQuery = em.createNamedQuery("User.findAllUsers", User.class);
		List<User> resultList = findAllUsersQuery.getResultList();
		return resultList;
	}

}
