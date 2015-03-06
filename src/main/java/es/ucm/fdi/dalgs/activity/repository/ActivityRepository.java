package es.ucm.fdi.dalgs.activity.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.LearningGoal;

@Repository
public class ActivityRepository {
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

	public boolean addActivity(Activity activity) {

		try {
			em.persist(activity);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Activity> getAll() {

		return em
				.createQuery(
						"select a from Activity a inner join a.course s  where a.isDeleted='false' order by a.course")
				.getResultList();
	}

	public boolean saveActivity(Activity activity) {
		try {
			em.merge(activity);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public Activity getActivity(Long id) {
		return em.find(Activity.class, id);

	}

	public boolean deleteActivity(Long id) {
		Activity activity = em.getReference(Activity.class, id);
		activity.setDeleted(true);
		try {

			em.merge(activity);
			// em.remove(activity);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Activity> getActivitiesForCourse(Long id_course) {
		Course course = em.getReference(Course.class, id_course);

		Query query = em
				.createQuery("select a from Activity a where a.course=?1 and a.isDeleted='false' ");
		query.setParameter(1, course);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}

	public Activity existByCode(String code) {
		Query query = em.createQuery("Select a from Activity a where a.info.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Activity) query.getSingleResult();
	}

	public String getNextCode() {
		Query query = em.createQuery("Select MAX(e.id ) from Activity e");
		try {
			Long aux = (Long) query.getSingleResult() + 1;
			return "ACT" + aux;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public Activity getActivityByName(String name) {
		Query query = em
				.createQuery("select a from Activity a where a.info.name=?1");
		query.setParameter(1, name);

		return (Activity) query.getResultList().get(0);

	}
	
	@SuppressWarnings("unchecked")
	public Collection<Activity> getActivitiesForLearningGoal(
			LearningGoal learningGoal) {
//		LearningGoal learning =em.getReference(LearningGoal.class, id_learningGoal);
		Query query = em.createQuery("SELECT a FROM Activity a JOIN a.learningGoalStatus l WHERE l.learningGoal = ?1");
		query.setParameter(1, learningGoal);
		
		return (Collection<Activity>)query.getResultList();
	}
	public boolean deleteActivitiesFromCourses(Collection<Course> courses) {

		try {
			Query query = em
					.createQuery("UPDATE Activity a SET a.isDeleted = true where a.course in ?1");

			query.setParameter(1, courses);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());

			return false;
		}
		return true;
	}
	
	public boolean deleteActivitiesFromCourse(Course course) {

		try {
			Query query = em
					.createQuery("UPDATE Activity a SET a.isDeleted = true where a.course = ?1");

			query.setParameter(1, course);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());

			return false;
		}
		return true;
	}

}
