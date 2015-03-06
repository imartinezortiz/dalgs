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
		return em.createQuery("select t from Topic t where t.isDeleted = false order by t.id ")
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


	public Topic getTopic(Long id) {
		return em.find(Topic.class, id);
	}


	public boolean deleteTopic(Topic topic) {
		try {
//			Topic topic = em.getReference(Topic.class, id_topic);
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
		Query query = em.createQuery("select t from Topic t where t.info.code=?1 and t.module = ?2");
		query.setParameter(1, code);
		query.setParameter(2, module);
		 if (query.getResultList().isEmpty())
		 	return null;
		 else return (Topic) query.getSingleResult();
	}

	
	@SuppressWarnings("unchecked")
	public Collection<Topic> getTopicsForModule(Long id) {
		Module module = em.getReference(Module.class, id);

		Query query = em
				.createQuery("select t from Topic t where t.module=?1 and t.isDeleted='false'");
		query.setParameter(1, module);

		if (query.getResultList().isEmpty())
			return null;
		return (List<Topic>) query.getResultList();
	}


	@SuppressWarnings("unchecked")
	public Collection<Topic> getTopicsForModules(Collection<Module> modules) {
		Query query = em.createQuery("SELECT t  FROM Topic t WHERE t.isDeleted = false  AND t.module in ?1");
		query.setParameter(1, modules);
		
		if (query.getResultList().isEmpty()) return null;
		else return (Collection<Topic>) query.getResultList();
		
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

	
}
