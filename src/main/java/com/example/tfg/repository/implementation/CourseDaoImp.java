package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.CourseDao;

@Repository
public class CourseDaoImp implements CourseDao {

	protected EntityManager em;
	protected static final Logger logger = LoggerFactory
			.getLogger(CourseDaoImp.class);

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

	@Override
	public boolean addCourse(Course course) {
		try {
			em.persist(course);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
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
	public boolean saveCourse(Course course) {
		try {
			em.merge(course);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
			return false;
		}
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<Course> getCoursesByAcademicTerm(String term) {
	 * Query query = em.createQuery(
	 * "select c from Course c  join c.academicTerm t where t.term=?1");
	 * query.setParameter(1, term);
	 * 
	 * return query.getResultList(); }
	 */

	public boolean exist(Course course) {
		Query query = em
				.createQuery("select c from Course c  where c.academicTerm=?1 and c.subject=?2");
		query.setParameter(1, course.getAcademicTerm());
		query.setParameter(2, course.getSubject());

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}

	@SuppressWarnings("unchecked")
	public List<Course> getCoursesByAcademicTerm(Long academic_id) {
		AcademicTerm academic = em
				.getReference(AcademicTerm.class, academic_id);

		Query query = em
				.createQuery("select c from Course c  join c.academicTerm a  where a=?1");
		query.setParameter(1, academic);

		if (query.getResultList().isEmpty())
			return null;
		else
			return query.getResultList();

	}

	public Long isDisabled(Long id_academic, Long id_subject) {
		Subject subject = em.getReference(Subject.class, id_subject);
		AcademicTerm academic = em
				.getReference(AcademicTerm.class, id_academic);

		Query query = em
				.createQuery("select c from Course c where c.subject=?1 and c.academicTerm=?2 and c.isDeleted=1");
		query.setParameter(1, subject);
		query.setParameter(2, academic);

		if (query.getResultList().isEmpty())
			return null;

		Course aux = (Course) query.getSingleResult();
		return aux.getId();
	}

	@Override
	public boolean deleteCoursesFromAcademic(AcademicTerm academic) {
		try {
			Query query = em
					.createQuery("UPDATE Course c SET c.isDeleted = true where c.academicTerm=?1");

			query.setParameter(1, academic);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;

	}

}
