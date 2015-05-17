/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.dalgs.user.repository;

import java.util.Collection;
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
	public List<User> getAll(Integer pageIndex, Boolean showAll,
			String typeOfUser) {
		Query query = null;
		if (showAll) {
			query = em
					.createQuery("select u from User u join u.roles ur where ur.role =?1 order by u.id");
			query.setParameter(1, typeOfUser);

		} else {
			query = em
					.createQuery("select u from User u join u.roles ur where ur.role =?1 and u.enabled = '00000001' order by u.id");
			query.setParameter(1, typeOfUser);

		}
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

		Query query = em.createNativeQuery("select count(*) from user");
		logger.info(query.getSingleResult().toString());
		double dou = Double.parseDouble(query.getSingleResult().toString())
				/ ((double) noOfRecords);
		return (int) Math.ceil(dou);

	}

	public User getUser(Long id_user) {
		return em.find(User.class, id_user);
	}

	public boolean persistListUsers(List<User> users) {
		int i = 0;
		for (User u : users) {
			try {

				// In this case we have to hash the password (SHA-256)
				// StringSHA sha = new StringSHA();
				// String pass = sha.getStringMessageDigest(u.getPassword());
				// u.setPassword(pass);

				u.setId(null); // If not a detached entity is passed to persist
				
				if (findByEmail(u.getEmail())==null)
					em.persist(u);
					// em.flush();

				if (++i % 20 == 0) {
					em.flush();
				}
			} catch (Exception e) {
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

		if (query.getResultList().isEmpty()) {
			logger.error("User not found by username " + username);
			return null;
		}

		return (User) query.getSingleResult();
	}

	public User findByEmail(String email) {
		Query query = em.createQuery("select s from User s where s.email=?1");
		query.setParameter(1, email);

		if (query.getResultList().isEmpty()) {
//			logger.error("User not found by email " + email);
			return null;
		}
		return (User) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllByRole(String user_role) {
		Query query = em
				.createQuery("select u from User u join u.roles ur where ur.role = ?1 order by u.id");
		query.setParameter(1, user_role);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<User> getAllUsers() {
		return em.createQuery("select u from User u join u.roles").getResultList();
	}

}