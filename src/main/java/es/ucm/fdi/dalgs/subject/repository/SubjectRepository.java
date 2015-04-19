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
import es.ucm.fdi.dalgs.domain.Module;
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

		return em
				.createQuery(
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

	public Subject getSubject(Long id, Long id_topic, Long id_module,
			Long id_degree) {
		Topic topic = em.getReference(Topic.class, id_topic);
		Module module = em.getReference(Module.class, id_module);
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("Select s from Subject s where s.id=?1 and s.topic=?2 and s.topic.module=?3 and s.topic.module.degree=?4");
		query.setParameter(1, id);
		query.setParameter(2, topic);
		query.setParameter(3, module);
		query.setParameter(4, degree);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Subject) query.getSingleResult();

	}

	public Subject getSubjectFormatter(Long id) {
		return em.find(Subject.class, id);

	}

	public boolean deleteSubject(Subject subject) {

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
	public List<Subject> getSubjectsForTopic(Long id_topic, Boolean show) {
		Topic topic = em.getReference(Topic.class, id_topic);
		if (show) {
			Query query = em
					.createQuery("select s from Subject s where s.topic=?1");
			query.setParameter(1, topic);

			return (List<Subject>) query.getResultList();
		} else {

			Query query = em
					.createQuery("select s from Subject s where s.topic=?1 and s.isDeleted='false'");
			query.setParameter(1, topic);

			return (List<Subject>) query.getResultList();
		}
	}

	public Subject existByCode(String code) {
		Query query = em
				.createQuery("Select s from Subject s where s.info.code=?1");
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

	@SuppressWarnings("unchecked")
	public Collection<Subject> getSubjectsForTopics(Collection<Topic> topics) {

		Query query = em
				.createQuery("select s from Subject s where s.isDeleted='false' and s.topic in ?1");
		query.setParameter(1, topics);

		return (Collection<Subject>) query.getResultList();

	}

	public boolean persistListSubjects(List<Subject> subjects) {

		int i = 0;
		for (Subject s : subjects) {
			try {

				s.setId(null); // If not a detached entity is passed to persist
				em.persist(s);

				if (++i % 20 == 0) {
					em.flush();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			}
		}

		return true;

	}

}
