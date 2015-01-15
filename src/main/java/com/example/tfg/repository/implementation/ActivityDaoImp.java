package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Course;
import com.example.tfg.repository.ActivityDao;

@Repository
public class ActivityDaoImp implements ActivityDao {
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		try {
			this.em = entityManager;
		} catch (Exception e) {
		}
	}

	public boolean addActivity(Activity activity) {

		try {
			em.persist(activity);
			return true;
		} catch (ConstraintViolationException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Activity> getAll() {

		return em
				.createQuery(
						"select a from Activity a inner join a.course s order by a.course")
				.getResultList();
	}

	public boolean saveActivity(Activity activity) {
		try {
			em.merge(activity);
			return true;
		} catch (ConstraintViolationException e) {
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
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Activity> getActivitiesForCourse(Long id_course) {
		Course course = em.getReference(Course.class, id_course);

		Query query = em
				.createQuery("select a from Activity a where a.course=?1");
		query.setParameter(1, course);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}

	public boolean existByCode(String code) {
		Query query = em.createQuery("from Activity a where a.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}

	public String getNextCode() {
		Query query = em.createQuery("Select MAX(e.id ) from Activity e");
		try {
			Long aux = (Long) query.getSingleResult() + 1;
			return "ACT" + aux;
		} catch (Exception e) {
			return null;
		}

	}
	public Activity getActivityByName(String name) {
		Query query = em.createQuery("select c from Activity c where c.name=?1");
		query.setParameter(1, name);
		
		return (Activity) query.getResultList().get(0);

	}

	
	

}
