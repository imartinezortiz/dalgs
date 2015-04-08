package es.ucm.fdi.dalgs.learningGoal.repository;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.LearningGoal;

@Repository
public class LearningGoalRepository {

	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(LearningGoalRepository.class);

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

	public boolean addLearningGoal(LearningGoal newLearningGoal) {

		try {
			em.persist(newLearningGoal);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	public boolean saveLearningGoal(LearningGoal existLearning) {
		try {
			em.merge(existLearning);
			return true;
		} catch (ConstraintViolationException e) {
			return false;
		}
	}

	public LearningGoal existByCode(String code) {

		Query query = em
				.createQuery("Select l from LearningGoal l where l.info.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (LearningGoal) query.getSingleResult();
	}

	public LearningGoal getLearningGoal(Long id_learningGoal,
			Long id_competence, Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);
		Competence competence = em
				.getReference(Competence.class, id_competence);

		Query query = em
				.createQuery("Select l from LearningGoal l where l.id=?1 and l.competence=?2 and l.competence.degree=?3");
		query.setParameter(1, id_learningGoal);
		query.setParameter(2, competence);
		query.setParameter(3, degree);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (LearningGoal) query.getSingleResult();
	}

	public LearningGoal getLearningGoalFormatter(Long id_learningGoal) {
		return em.find(LearningGoal.class, id_learningGoal);
	}

	@SuppressWarnings("unchecked")
	public Collection<LearningGoal> getLearningGoalsFromCourse(Long id_course,
			Collection<LearningGoal> LearningGoals) {
		try {

			Course course = em.getReference(Course.class, id_course);
			Query query = em
					.createQuery("SELECT l FROM Course c JOIN c.subject s "
							+ "JOIN s.competences x JOIN x.learningGoals l WHERE l NOT IN ?2 AND l.isDeleted = false AND c=?1");

			query.setParameter(1, course);
			query.setParameter(2, LearningGoals);

			return (Collection<LearningGoal>) query.getResultList();
		} catch (Exception e) {

			logger.error(e.getMessage());
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public Collection<LearningGoal> getLearningGoalsFromCourse(Long id_course) {
		Course course = em.getReference(Course.class, id_course);
		Query query = em
				.createQuery("SELECT l FROM Course c JOIN c.subject s "
						+ "JOIN s.competences x JOIN x.learningGoals l WHERE l.isDeleted = false AND c=?1");

		query.setParameter(1, course);
		return (Collection<LearningGoal>) query.getResultList();
	}

	public boolean deleteLearningGoal(LearningGoal learningGoal) {
		learningGoal.setDeleted(true);

		try {
			em.merge(learningGoal);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean deleteLearningGoalForCompetence(Competence competence) {
		try {
			Query query = em
					.createQuery("UPDATE LearningGoal l SET l.isDeleted = true where l.competence=?1");

			query.setParameter(1, competence);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public LearningGoal getLearningGoalByName(String name) {

		Query query = em
				.createQuery("SELECT l FROM LearningGoal l WHERE isDeleted = false AND l.name = ?1");
		query.setParameter(1, name);

		return (LearningGoal) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Collection<LearningGoal> getLearningGoalsFromActivity(
			Activity activity) {
		Query query1 = em
				.createQuery("SELECT l FROM Activity a INNER JOIN a.learningGoalStatus s "
						+ "INNER JOIN s.learningGoal l WHERE a=?1");
		query1.setParameter(1, activity);
		return (Collection<LearningGoal>) query1.getResultList();

	}

	@SuppressWarnings("unchecked")
	public Collection<LearningGoal> getLearningGoalsFromCompetence(
			Competence competence, Boolean show) {
		if (show) {
			Query query = em
					.createQuery("select l from LearningGoal l where l.competence=?1");
			query.setParameter(1, competence);
			return query.getResultList();
		} else {
			Query query = em
					.createQuery("select l from LearningGoal l where l.competence=?1 and l.isDeleted='false'");
			query.setParameter(1, competence);
			return query.getResultList();
		}
	}

	public boolean deleteLearningGoalsForCompetences(
			Collection<Competence> competences) {
		try {
			Query query = em
					.createQuery("UPDATE LearningGoal l SET l.isDeleted = true where l.competence IN ?1");

			query.setParameter(1, competences);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

}
