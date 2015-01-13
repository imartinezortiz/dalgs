package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.SubjectDao;

@Repository
public class SubjectDaoImp implements SubjectDao {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	public boolean addSubject(Subject subject) {

		try {
			em.persist(subject);
			return true;
		} catch (ConstraintViolationException e) {
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
		return  (List<Subject>) query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Subject> getSubjectsForCompetence(long id) {
		Competence competence = em.getReference(Competence.class, id);
		Query query = em.createQuery
		// .createQuery("select c from Competence c where c.subject=?1");
				("select s from Subject s JOIN s.competences c where c = ?1");
		query.setParameter(1, competence);
		if (query.getResultList().isEmpty())
			return null;
		return query.getResultList();
	}

	public String getNextCode() {
		Query query = em.createQuery("Select MAX(e.id ) from Subject e");
		try {
			Long aux = (Long) query.getSingleResult() + 1;
			return "SUB" + aux;
		} catch (Exception e) {
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
	
	public Subject getSubjectForCourse(long id_course) {
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
		if (query.getResultList().isEmpty())
			return null;
		return (Subject) query.getResultList().get(0);

	}

}
