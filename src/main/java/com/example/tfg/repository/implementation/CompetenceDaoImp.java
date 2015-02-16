package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.CompetenceDao;

@Repository
public class CompetenceDaoImp implements CompetenceDao {

	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(CompetenceDaoImp.class);

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

	public Competence existByCode(String code, Degree degree) {
		Query query = em
				.createQuery("Select c from Competence c where c.info.code=?1 and c.degree=?2");
		query.setParameter(1, code);
		query.setParameter(2, degree);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Competence) query.getSingleResult();
	}

	public boolean addCompetence(Competence competence) {
		
		try {
			em.persist(competence);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
		
		
//		Query query = em
//				.createQuery("select p from Competence p where p.name=?1");
//		query.setParameter(1, competence.getInfo().getName());
//
//		@SuppressWarnings("unchecked")
//		List<Subject> p = query.getResultList();
//		if (p.isEmpty()) {
//			try {
//				em.persist(competence);
//			} catch (ConstraintViolationException e) {
//				logger.error(e.getMessage());
//				return false;
//			}
//
//		} else
//			return false;
//		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Competence> getAll() {
		return em
				.createQuery(
						"select c from Competence c inner join c.degree where c.isDeleted='false' d order by c.id")

				.getResultList();
	}

	public boolean saveCompetence(Competence competence) {
		try {
			em.merge(competence);
			return true;
		} catch (ConstraintViolationException e) {
			return false;
		}
	}

	public Competence getCompetence(Long id) {
		return em.find(Competence.class, id);

	}

	public boolean deleteCompetence(Long id) {
		Competence competence = this.getCompetence(id);
		competence.setDeleted(true);

		try {
			em.merge(competence);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Competence> getCompetencesForSubject(Long id_subject) {
		Subject subject = em.getReference(Subject.class, id_subject);

		Query query = em
				.createQuery("select c from Subject s join s.competences c where s = ?1 and c.isDeleted='false'");
		query.setParameter(1, subject);

		return (List<Competence>)query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Competence> getCompetencesForDegree(Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("select c from Competence c where c.degree=?1 and c.isDeleted='false'");
		query.setParameter(1, degree);
		return query.getResultList();
	}

	public Competence getCompetenceByName(String name) {
		Query query = em
				.createQuery("select c from Competence c where c.name=?1");
		query.setParameter(1, name);

		return (Competence) query.getResultList().get(0);

	}

	// public String getNextCode(){
	// Query query = em.createQuery("Select MAX(e.id ) from Competence e");
	// try {
	// Long aux = (Long) query.getSingleResult() + 1;
	// return "COMP" + aux;
	// } catch (Exception e) {
	// return null;
	// }
	//
	// }

	public boolean deleteCompetencesForDegree(Degree degree) {

		try {
			Query query = em
					.createQuery("UPDATE Competence c SET c.isDeleted = true where c.degree=?1");

			query.setParameter(1, degree);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
}
