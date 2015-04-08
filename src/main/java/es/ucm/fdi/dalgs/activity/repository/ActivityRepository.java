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

import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
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

	public Activity getActivityFormatter(Long id) {
		return em.find(Activity.class, id);
	}

	public Activity getActivity(Long id, Long id_course, Long id_group,
			Long id_academic) {

		Query query = null;
		Course course = em.getReference(Course.class, id_course);
		AcademicTerm academicterm = em.getReference(AcademicTerm.class,
				id_academic);

		if (id_group != null) {
			Group group = em.getReference(Group.class, id_group);
			query = em
					.createQuery("select a from Activity a where a.id=?1 and a.group=?2 and a.group.course=?3 and a.group.course.academicTerm=?4");
			query.setParameter(1, id);
			query.setParameter(2, group);
			query.setParameter(3, course);
			query.setParameter(4, academicterm);
		} else if (id_course != null) {
			query = em
					.createQuery("select a from Activity a where a.id=?1 and a.course=?2 and a.course.academicTerm=?3 ");
			query.setParameter(1, id);
			query.setParameter(2, course);
			query.setParameter(3, academicterm);

		}

		if (query.getResultList().isEmpty())
			return null;

		return (Activity) query.getSingleResult();

	}

	public boolean deleteActivity(Long id) {
		Activity activity = em.getReference(Activity.class, id);
		activity.getLearningGoalStatus().clear();
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
	public List<Activity> getActivitiesForCourse(Long id_course, Boolean showAll) {

		Course course = em.getReference(Course.class, id_course);
		Query query = null;

		if (!showAll) {
			query = em
					.createQuery("select a from Activity a where a.course=?1 and a.isDeleted='false' ");
		} else {
			query = em
					.createQuery("select a from Activity a where a.course=?1");
		}

		query.setParameter(1, course);
		// List<Activity> a = (List<Activity>)query.getResultList();
		return (List<Activity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Activity> getActivitiesForGroup(Long id_group, Boolean showAll) {

		Group group = em.getReference(Group.class, id_group);
		Query query = null;

		if (!showAll) {
			query = em
					.createQuery("select a from Activity a where a.group=?1 and a.isDeleted='false' ");
		} else {
			query = em.createQuery("select a from Activity a where a.group=?1");
		}

		query.setParameter(1, group);
		return query.getResultList();

	}

	public Activity existByCode(String code) {
		Query query = em
				.createQuery("Select a from Activity a where a.info.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Activity) query.getSingleResult();
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
		// LearningGoal learning =em.getReference(LearningGoal.class,
		// id_learningGoal);
		Query query = em
				.createQuery("SELECT a FROM Activity a JOIN a.learningGoalStatus l WHERE l.learningGoal = ?1");
		query.setParameter(1, learningGoal);

		return (Collection<Activity>) query.getResultList();
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

	public boolean deleteActivitiesFromGroup(Group group) {
		try {
			Query query = em
					.createQuery("UPDATE Activity a SET a.isDeleted = true where a.group = ?1");

			query.setParameter(1, group);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());

			return false;
		}
		return true;
	}

	public boolean deleteActivitiesFromGroups(Collection<Group> groups) {

		try {
			Query query = em
					.createQuery("UPDATE Activity a SET a.isDeleted = true where a.group in ?1");

			query.setParameter(1, groups);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());

			return false;
		}
		return true;
	}

}
