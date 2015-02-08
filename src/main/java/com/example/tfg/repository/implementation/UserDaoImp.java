package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;




import com.example.tfg.classes.StringSHA;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Role;
import com.example.tfg.domain.User;
import com.example.tfg.repository.UserDao;

//import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public class UserDaoImp implements UserDao {// extends JpaRepository<User, Long>
											// {
	private static final Integer noOfRecords = 20;

	protected static final Logger logger = LoggerFactory
			.getLogger(UserDaoImp.class);
	
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
		Query query = em.createQuery("select u from User u where u.isDeleted='false' order by u.id");
		
		return query.setMaxResults(noOfRecords)
				.setFirstResult(pageIndex * noOfRecords).getResultList();
	}

	@Override
	public boolean deleteUser(Long id) {
		User user = em.getReference(User.class, id);
		try {
			user.setDeleted(true);
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

	/*	Copetneces @SuppressWarnings("unchecked")
	public List<Competence> getCompetencesForSubject(Long id_subject) {
		Subject subject = em.getReference(Subject.class, id_subject);

		Query query = em
				.createQuery("select c from Subject s.competences c where s = ?1 and c.isDeleted='false'");
		query.setParameter(1, subject);

		return query.getResultList();
	}*/

	@SuppressWarnings("unchecked")
	public List<User> getAllByCourse(Long id_course,Integer pageIndex) {
		Course course = em.getReference(Course.class, id_course);

		Query query = em
				.createQuery("select u from Course c.users  where c = ?1 and u.isDeleted='false'");

		query.setParameter(1, course);

		return query.setMaxResults(noOfRecords)
				.setFirstResult(pageIndex * noOfRecords).getResultList();
	}

	@Override
	public boolean existInCourse(Long id, Long id_course) {
		Course course = em.getReference(Course.class, id_course);

		Query query = em
				.createQuery("select u from Course c.users  where c = ?1 and u.id=?2 ");

		query.setParameter(1, course);
		query.setParameter(2, id);
		
		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}

	public Integer numberOfPages() {

		Query query =em.createNativeQuery(
				"select count(*) from user where isDeleted='false'");
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
				Role role = new Role();
				role.setUser(u);
				role.setRole(2);
				u.setRole(role);
				
				//In this case we have to hash the password (SHA-256)
				StringSHA sha = new StringSHA();
				String pass = sha.getStringMessageDigest(u.getPassword());
				u.setPassword(pass);
				
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


}