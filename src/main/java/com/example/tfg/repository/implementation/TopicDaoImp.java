package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Topic> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveTopic(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Topic getTopic(Long id) {
		return em.find(Topic.class, id);
	}

	@Override
	public boolean deleteTopic(Long id_topic) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNextCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic existByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
