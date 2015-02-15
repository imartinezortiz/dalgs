package com.example.tfg.repository.implementation;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Module;
import com.example.tfg.domain.Topic;
import com.example.tfg.repository.TopicDao;

@Repository
public class TopicDaoImp implements TopicDao {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	protected static final Logger logger = LoggerFactory
			.getLogger(TopicDaoImp.class);

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


	public boolean deleteTopic(Long id_topic) {
		try {
			Module module = em.getReference(Module.class, id_topic);
			module.setDeleted(true);
			em.merge(module);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}


	public String getNextCode() {
	return "";
	}


	public Topic existByCode(String code) {
		Query query = em.createQuery("select p from Topic p where p.info.code=?1");
		query.setParameter(1, code);
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

}