package es.ucm.fdi.dalgs.externalActivity.repository;

import java.util.Collection;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.activity.repository.ActivityRepository;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.ExternalActivity;
import es.ucm.fdi.dalgs.domain.Group;

@Repository
public class ExternalActivityRepository {
	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(ActivityRepository.class);

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		try {
			this.em = entityManager;
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends ExternalActivity> getExternalActivitiesForGroup(
			long id_group) {
		Group group = em.getReference(Group.class, id_group);
		Query query = em.createQuery("from ExternalActivity ea where ea.group=?1 and ea.isDeleted='false'");
		
		query.setParameter(1, group);
		return (Collection<ExternalActivity>)query.getResultList();
	}

	public boolean addActivity(ExternalActivity act) {
	
		try {
			em.persist(act);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public ExternalActivity existByCode(String code) {
		Query query = em
				.createQuery("Select a from ExternalActivity a where a.info.code=?1 and a.isDeleted='false'");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (ExternalActivity) query.getSingleResult();
	}

	public ExternalActivity getExternalActivity(Long id_externalActivity,
			Long id_course, Long id_group, Long id_academic) {
		
		Query query = null;
		Course course = em.getReference(Course.class, id_course);
		AcademicTerm academicterm = em.getReference(AcademicTerm.class,
				id_academic);

		if (id_group != null) {
			Group group = em.getReference(Group.class, id_group);
			query = em
					.createQuery("select a from ExternalActivity a where a.id=?1 and a.group=?2 and a.group.course=?3 and a.group.course.academicTerm=?4");
			query.setParameter(1, id_externalActivity);
			query.setParameter(2, group);
			query.setParameter(3, course);
			query.setParameter(4, academicterm);
		} else if (id_course != null) {
			query = em
					.createQuery("select a from ExternalActivity a where a.id=?1 and a.course=?2 and a.course.academicTerm=?3 ");
			query.setParameter(1, id_externalActivity);
			query.setParameter(2, course);
			query.setParameter(3, academicterm);

		}

		if (query.getResultList().isEmpty())
			return null;

		return (ExternalActivity) query.getSingleResult();
		
		
	}


	public boolean saveExternalActivity(ExternalActivity externalActivity) {
		try {
			em.merge(externalActivity);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public Boolean deleteExternalActivity(Long id_externalEexternalActivity) {
		ExternalActivity externalActivity = em.getReference(ExternalActivity.class, id_externalEexternalActivity);
//		externalActivity.getLearningGoalStatus().clear();
		externalActivity.setIsDeleted(true);
		try {

			em.merge(externalActivity);
			// em.remove(activity);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	
}
