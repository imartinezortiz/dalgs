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
package es.ucm.fdi.dalgs.topic.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.domain.Topic;

@Repository
public class TopicRepository {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	protected static final Logger logger = LoggerFactory
			.getLogger(TopicRepository.class);

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		try {
			this.em = entityManager;
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
	}

	public boolean addTopic(Topic topic) {
		try {
			em.persist(topic);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Topic> getAll() {
		return em
				.createQuery(
						"select t from Topic t where t.isDeleted = false order by t.id ")
				.getResultList();
	}

	public boolean saveTopic(Topic topic) {
		try {
			em.merge(topic);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public Topic getTopicFormatter(Long id) {
		return em.find(Topic.class, id);
	}

	public Topic getTopic(Long id, Long id_module, Long id_degree) {
		Module module = em.getReference(Module.class, id_module);
		Degree degree = em.getReference(Degree.class, id_degree);
		Query query = em
				.createQuery("select t from Topic t where t.id=?1 and t.module = ?2 and t.module.degree=?3");
		query.setParameter(1, id);
		query.setParameter(2, module);
		query.setParameter(3, degree);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Topic) query.getSingleResult();
	}

	public boolean deleteTopic(Topic topic) {
		try {
			topic.setDeleted(true);
			em.merge(topic);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public String getNextCode() {
		return "";
	}

	public Topic existByCode(String code, Long id_module) {
		Module module = em.getReference(Module.class, id_module);
		Query query = em
				.createQuery("select t from Topic t where t.info.code=?1 and t.module = ?2");
		query.setParameter(1, code);
		query.setParameter(2, module);
		if (query.getResultList().isEmpty())
			return null;
		else
			return (Topic) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Collection<Topic> getTopicsForModule(Long id, Boolean show) {
		Module module = em.getReference(Module.class, id);
		if (show) {
			Query query = em
					.createQuery("select t from Topic t where t.module=?1");
			query.setParameter(1, module);

			return (List<Topic>) query.getResultList();
		} else {

			Query query = em
					.createQuery("select t from Topic t where t.module=?1 and t.isDeleted='false'");
			query.setParameter(1, module);

			return (List<Topic>) query.getResultList();

		}
	}

	@SuppressWarnings("unchecked")
	public Collection<Topic> getTopicsForModules(Collection<Module> modules) {
		Query query = em
				.createQuery("SELECT t  FROM Topic t WHERE t.isDeleted = false  AND t.module in ?1");
		query.setParameter(1, modules);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Collection<Topic>) query.getResultList();

	}

	public boolean deleteTopicsForModules(Collection<Module> modules) {
		try {

			Query query = em
					.createQuery("UPDATE Topic t SET t.isDeleted = true where t.module IN ?1");
			query.setParameter(1, modules);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	public boolean deleteTopicsForModule(Module module) {
		try {

			Query query = em
					.createQuery("UPDATE Topic t SET t.isDeleted = true where t.module = ?1");
			query.setParameter(1, module);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean persistListTopics(List<Topic> topics) {

		int i = 0;
		for (Topic t : topics) {
			try {

				// In this case we have to hash the password (SHA-256)
				// StringSHA sha = new StringSHA();
				// String pass = sha.getStringMessageDigest(u.getPassword());
				// u.setPassword(pass);

				t.setId(null); // If not a detached entity is passed to persist
				em.persist(t);
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

}
