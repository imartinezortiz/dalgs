package com.example.tfg.repository.implementation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.User;
import com.example.tfg.repository.UserDao;

//import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public class UserDaoImp implements UserDao {// extends JpaRepository<User, Long>
											// {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	public User findByUsername(String username) {
		Query query = em
				.createQuery("select s from User s where s.username=?1");
		query.setParameter(1, username);

		return (User) query.getSingleResult();
	}
}