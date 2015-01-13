package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.CompetenceDao;

@Repository
public class CompetenceDaoImp implements CompetenceDao {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	public boolean existByCode(String code) {
		Query query = em.createQuery("from Competence a where a.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}
	
	public boolean addCompetence(Competence competence) {
		Query query = em
				.createQuery("select p from Competence p where p.name=?1");
		query.setParameter(1, competence.getName());

		@SuppressWarnings("unchecked")
		List<Subject> p = query.getResultList();
		if (p.isEmpty()) {
			try {
				em.persist(competence);
			} catch (ConstraintViolationException e) {
				return false;
			}
			
		} else
			return false;
		return true;
	}


	@SuppressWarnings("unchecked")
	public List<Competence> getAll() {
		return em.createQuery("select c from Competence c inner join c.degree d order by c.id")
			//	"from Competence a inner join a.subject s order by a.id")
				.getResultList();
	}

	public boolean saveCompetence(Competence competence) {
		try{
			em.merge(competence);
			return true;
		} catch (ConstraintViolationException e) {
			return false;
		}
	}

	public Competence getCompetence(long id) {
		return em.find(Competence.class, id);

	}

	
	public boolean deleteCompetence(long id) {
		Competence competence = this.getCompetence(id);
		competence.setDeleted(true);

		try {
			em.merge(competence);
			//em.remove(competence);

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Competence> getCompetencesForSubject(long id_subject) {
		Subject subject = em.getReference(Subject.class, id_subject);
	    //Hibernate.initialize(subject);

		Query query = em.createQuery
		//		.createQuery("select c from Competence c where c.subject=?1");
				("select c from Competence c JOIN c.subjects s where s = ?1");
		query.setParameter(1, subject);
		if (query.getResultList().isEmpty())
			return null;
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Competence> getCompetencesForDegree(long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);
	    Hibernate.initialize(degree.getId());

		Query query = em
				.createQuery("select c from Competence c where c.degree=?1");
		query.setParameter(1, degree);
		if (query.getResultList().isEmpty())
			return null;
		return query.getResultList();
	}
	

	public Competence getCompetenceByName(String name) {
		Query query = em.createQuery("select c from Competence c where c.name=?1");
		query.setParameter(1, name);
		if (query.getResultList().isEmpty())
			return null;
		return (Competence) query.getResultList().get(0);

	}
	public String getNextCode(){
		Query query = em.createQuery("Select MAX(e.id ) from Competence e");
		try {
			Long aux = (Long) query.getSingleResult() + 1;
			return "COMP" + aux;
		} catch (Exception e) {
			return null;
		}

	}
}
