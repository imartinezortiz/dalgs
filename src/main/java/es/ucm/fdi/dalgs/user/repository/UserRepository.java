package es.ucm.fdi.dalgs.user.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.User;

@Repository
public class UserRepository {
	private static final Integer noOfRecords = 20;

	protected static final Logger logger = LoggerFactory
			.getLogger(UserRepository.class);
	
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}
	
	
	public boolean addUser(User user) {
		try {

			em.persist(user);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public List<User> getAll(Integer pageIndex) {
		Query query = em.createQuery("select u from User u  order by u.id");
		
		return query.setMaxResults(noOfRecords)
				.setFirstResult(pageIndex * noOfRecords).getResultList();
	}

	
	public boolean deleteUser(Long id) {
		User user = em.getReference(User.class, id);
		try {
			user.setEnabled(false);
			em.merge(user);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	
	public boolean saveUser(User user) {
		try {
			em.merge(user);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}


	public Integer numberOfPages() {

		Query query =em.createNativeQuery(
				"select count(*) from user");
		logger.info(query.getSingleResult().toString());
		double dou = Double.parseDouble(query.getSingleResult().toString())/ ((double) noOfRecords);
		return (int) Math.ceil(dou);

	}

	public User getUser(Long id_user) {
		return em.find(User.class, id_user);
	}

	public boolean persistALotUsers(List<User> users){
		int i = 0;
		for(User u : users) {
			try{
				
				//In this case we have to hash the password (SHA-256)
				//StringSHA sha = new StringSHA();
				//String pass = sha.getStringMessageDigest(u.getPassword());
				//u.setPassword(pass);
				
				u.setId(null); //If not  a detached entity is passed to persist
				em.persist(u);
		    	//em.flush();


		    if(++i % 20 == 0) {
		    	em.flush();
		    }
			}catch(Exception e){
				logger.error(e.getMessage());
				return false;
			}
		}
		
		return true;

	}
	
	// Logging
	public User findByUsername(String username) {
		Query query = em
				.createQuery("select s from User s where s.username=?1");
		query.setParameter(1, username);
		
		if(query.getResultList().isEmpty()){
			logger.error("User not found by username " + username);
			return null;
		}

		return (User) query.getSingleResult();
	}

	public User findByEmail(String email) {
		Query query = em
				.createQuery("select s from User s where s.email=?1");
		query.setParameter(1, email);

		if(query.getResultList().isEmpty()) {
			logger.error("User not found by email " + email);
			return null;
		}
		return (User) query.getSingleResult();
	}


}