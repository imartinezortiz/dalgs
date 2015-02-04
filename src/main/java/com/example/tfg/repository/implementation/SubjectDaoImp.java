package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Course;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.SubjectDao;

@Repository
public class SubjectDaoImp implements SubjectDao {

	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(SubjectDaoImp.class);

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

	public boolean addSubject(Subject subject) {

		try {
			em.persist(subject);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Subject> getAll() {

		return em.createQuery(
				"select s from Subject s inner join s.degree d order by s.id")
				.getResultList();
	}

	public boolean saveSubject(Subject subject) {
		try {
			em.merge(subject);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;

		}
	}

	public Subject getSubject(Long id) {
		return em.find(Subject.class, id);

	}

	public boolean deleteSubject(Long id) {
		// Subject subject = this.getSubject(id);
		Subject subject = em.getReference(Subject.class, id);
		try {
			subject.setDeleted(true);
			em.merge(subject);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Subject> getSubjectsForDegree(Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("select s from Subject s where s.degree=?1");
		query.setParameter(1, degree);

		if (query.getResultList().isEmpty())
			return null;
		List<Subject> s = (List<Subject>) query.getResultList();
		return s;
	}

	// @SuppressWarnings("unchecked")
	// public List<Subject> getSubjectsForCompetence(Long id) {
	// Competence competence = em.getReference(Competence.class, id);
	// Query query = em.createQuery
	// //
	// createQuery("select c from Subject s join s.competences c where c=?1");
	// ("select s from Subject s JOIN s.competences c where c = ?1");
	// query.setParameter(1, competence);
	//
	// Competence c = (Competence)query.getSingleResult();
	// return query.getResultList();
	// }

	public String getNextCode() {
		Query query = em.createQuery("Select MAX(e.id ) from Subject e");
		try {
			Long aux = (Long) query.getSingleResult() + 1;
			return "SUB" + aux;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public boolean existByCode(String code) {
		Query query = em.createQuery("from Subject s where s.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}

	public Subject getSubjectForCourse(Long id_course) {
		Course course = em.getReference(Course.class, id_course);

		Query query = em
				.createQuery("select s from Subject s join s.courses c where c=?1");
		query.setParameter(1, course);

		if (query.getResultList().isEmpty())
			return null;

		return (Subject) query.getSingleResult();
	}

	public Subject getSubjectByName(String name) {
		Query query = em.createQuery("select c from Subject c where c.name=?1");
		query.setParameter(1, name);

		return (Subject) query.getResultList().get(0);

	}

	public boolean deleteSubjectsForDegree(Degree degree) {

		try {

			Query query = em
					.createQuery("UPDATE Subject c SET c.isDeleted = true where c.degree=?1");
			query.setParameter(1, degree);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	@Override
	public boolean addSubjects(List<Subject> s) {
		try {
			for (Subject subject : s)
				em.persist(subject);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

}
