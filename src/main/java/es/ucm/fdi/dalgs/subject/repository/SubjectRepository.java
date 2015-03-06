package es.ucm.fdi.dalgs.subject.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.Topic;

@Repository
public class SubjectRepository {

	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(SubjectRepository.class);

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
				"select s from Subject s inner join s.topic d where s.isDeleted='false' order by s.id")
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
	public List<Subject> getSubjectsForTopic(Long id_topic) {
		Topic topic = em.getReference(Topic.class, id_topic);

		Query query = em
				.createQuery("select s from Subject s where s.topic=?1 and s.isDeleted='false'");
		query.setParameter(1, topic);

		if (query.getResultList().isEmpty())
			return null;
		List<Subject> s = (List<Subject>) query.getResultList();
		return s;
	}


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

	public Subject existByCode(String code) {
		Query query = em.createQuery("Select s from Subject s where s.info.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Subject) query.getSingleResult();
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

	
	@SuppressWarnings("unchecked")
	public Collection<Subject> getSubjectForDegree(Degree degree) {
		Query query = em.createQuery("SELECT s FROM Subject s JOIN s.topic t "
				+ "JOIN t.module m JOIN m.degree d WHERE d = ?1");
		query.setParameter(1, degree);
		
		return (Collection<Subject>) query.getResultList();
	}


	public boolean deleteSubjectsForTopics(Collection<Topic> topics) {
		try {

			Query query = em
					.createQuery("UPDATE Subject s SET s.isDeleted = true where s.topic IN ?1");
			query.setParameter(1, topics);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

}
