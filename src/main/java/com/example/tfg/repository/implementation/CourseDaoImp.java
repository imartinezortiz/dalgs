package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Degree;
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
	public List<Course> getCoursesByAcademicTerm(String term) { 
		Query query = em.createQuery("select c from Course c  join c.academicTerm t where t.term=?1");
		query.setParameter(1, term);
 
		return query.getResultList();
	}

	

	public boolean exist(Course course) {
		Query query = em.createQuery("select c from Course c  where c.academicTerm=?1 and c.subject=?2");
		query.setParameter(1, course.getAcademicTerm());
		query.setParameter(2, course.getSubject());
 
		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}

	@SuppressWarnings("unchecked")
	public List<Course> getCoursesByAcademicTermDegree(String term,
			Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em.createQuery("select c from Course c  join c.academicTerm a  where a.term=?1 and a.degree=?2");
		query.setParameter(1, term);
		query.setParameter(2, degree);
 
		if (query.getResultList().isEmpty())
			return null;
		else
			return query.getResultList();
	
	}



}
