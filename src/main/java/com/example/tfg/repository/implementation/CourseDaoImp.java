package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;
import com.example.tfg.repository.CourseDao;

@Repository
public class CourseDaoImp implements CourseDao {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	@Override
	public boolean addCourse(Course course) {
		try {
			em.persist(course);
		} catch (ConstraintViolationException e) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Course> getAll() {
		return em.createQuery("select d from Course d order by d.id")
				.getResultList();
	}

	@Override
	public boolean saveSubject(Course course) {
		try {
			em.merge(course);
		} catch (ConstraintViolationException e) {
			return false;
		}
		return true;
	}

	@Override
	public Course getCourse(Long id) {

		return em.find(Course.class, id);
	}

	@Override
	public boolean deleteCourse(Long id) {
		Course course = em.getReference(Course.class, id);
		try {
			course.setDeleted(true);
			em.merge(course);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Course> getCoursesByAcademicTerm(AcademicTerm aT) { 
		Query query = em.createQuery("select c from Course c  join c.academicTerm t where t.id=?1");
		query.setParameter(1, aT.getId());
 
		return query.getResultList();
	}

	public boolean existByName(String name) {
		Query query = em.createQuery("from Course c  where c.name=?1");
		query.setParameter(1, name);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}

	public Course getCourseByName(String name) {
		Query query = em.createQuery("select c from Course c where c.name=?1");
		query.setParameter(1, name);
		
		return (Course) query.getResultList().get(0);

	}

}
